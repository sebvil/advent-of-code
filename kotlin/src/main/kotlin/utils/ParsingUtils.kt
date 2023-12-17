package utils

import org.intellij.lang.annotations.Language
import sun.security.util.Password
import y23.Day11
import y23.Day13
import y23.Day14
import y23.Day16
import kotlin.enums.enumEntries
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Enum<T>> enumFromString(text: String): T {
    return enumEntries<T>().find { it.name.lowercase() == text.lowercase() }!!
}

fun <T : Enum<T>> enumFromString(clazz: KClass<T>, text: String): T {
    return clazz.java.enumConstants.find { it.name.lowercase() == text.lowercase() }!!
}

fun <T: ParsableEnum<T>> parsableEnumFromString(clazz: KClass<T>, text: String): T {
    return ParsableEnum.entries(clazz).find { it.stringValue.lowercase() == text.lowercase() }!!
}


interface ParsableEnum<T: ParsableEnum<T>> {
    val stringValue: String

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <P: ParsableEnum<P>> entries(clazz: KClass<P>): List<P> {
            return when (clazz) {
                Day11.SpaceCell::class -> Day11.SpaceCell.entries() as List<P>
                Day14.CellType::class -> Day14.CellType.entries() as List<P>
                Day16.CellType::class -> Day16.CellType.entries() as List<P>
                else -> error("Class not found")
            }
        }
    }
}


interface RegexParsable

data class RegexMatcher(val regex: Regex, val subMatchers: Map<Int, RegexMatcher>) {

    class Builder(@Language("RegExp") private val regex: String) {
        private var subMatchers: MutableMap<Int, RegexMatcher> = mutableMapOf()

        infix fun RegexMatcher.forGroup(index: Int) {
            this@Builder.subMatchers[index] = this
        }

        fun build(): RegexMatcher = RegexMatcher(regex = Regex(regex), subMatchers = subMatchers.toMap())

    }
}

fun regex(@Language("RegExp") pattern: String, buildAction: RegexMatcher.Builder.() -> Unit = {}): RegexMatcher {
    return RegexMatcher.Builder(pattern).apply(buildAction).build()
}

fun <T : Any> instancesFromRegex(
    clazz: KClass<T>,
    text: String,
    @Language("RegExp") pattern: String,
    buildAction: RegexMatcher.Builder.() -> Unit = {}
) = instancesFromRegex(clazz, text, regex(pattern, buildAction))

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun <T : Any> instancesFromRegex(
    clazz: KClass<T>,
    text: String,
    regex: RegexMatcher,
): List<T> {
//    println("$clazz, $text, ${regex.regex.pattern}")
    when (clazz) {
        Int::class -> return regex.regex.findAll(text)
            .flatMap {
                it.groupValues.subList(1, it.groupValues.size)
            }.map {
                it.toInt() as T
            }.toList()

        Long::class -> return regex.regex.findAll(text)
            .flatMap {
                it.groupValues.subList(1, it.groupValues.size)
            }.map {
                it.toLong() as T
            }.toList()

        String::class -> return regex.regex.findAll(text)
            .flatMap { it.groupValues.subList(1, it.groupValues.size) }.map { it as T }.toList()

        Boolean::class -> return listOf(text.toBoolean() as T)
    }
    when {
        clazz.starProjectedType.isSubtypeOf(Enum::class.starProjectedType) -> return regex.regex.findAll(text)
            .flatMap {
                it.groupValues.subList(1, it.groupValues.size)
            }.map {
                enumFromString(clazz as KClass<out Enum<*>>, it) as T
            }.toList()
        clazz.starProjectedType.isSubtypeOf(ParsableEnum::class.starProjectedType) -> return regex.regex.findAll(text)
            .flatMap {
                it.groupValues.subList(1, it.groupValues.size)
            }.map {
                parsableEnumFromString(clazz as KClass<out ParsableEnum<*>>, it) as T
            }.toList()
    }
    val constructor = clazz.primaryConstructor!!
    val typeTransform = { idx: Int, match: String ->
        val param = constructor.parameters[idx]
        val type = param.type
        when (val erasure = type.jvmErasure) {
            Int::class -> match.toInt()
            Long::class -> match.toLong()
            Boolean::class -> match.toBoolean()
            String::class -> match
            else -> when {
                type.isSubtypeOf(Enum::class.starProjectedType) -> enumFromString(erasure as KClass<out Enum<*>>, match)
                type.isSubtypeOf(ParsableEnum::class.starProjectedType) -> parsableEnumFromString(erasure as KClass<out ParsableEnum<*>>, match)
                type.isSubtypeOf(List::class.starProjectedType) -> {
                    val listType = type.arguments.first().type!!.jvmErasure
                    instancesFromRegex(listType, match, regex.subMatchers[idx]!!)
                }

                type.isSubtypeOf(RegexParsable::class.starProjectedType) -> {
                    instancesFromRegex(erasure as KClass<RegexParsable>, match, regex.subMatchers[idx]!!).first()
                }

                else -> error("Not supported: $type")
            }
        }
    }
    return regex.regex.findAll(text).map {
        constructor.call(*(it.destructured.toList().mapIndexed(typeTransform).toTypedArray()))
    }.toList()
}


inline fun <reified T : Any> instanceFromRegex(
    text: String,
    regex: RegexMatcher
): T {
    return instancesFromRegex(T::class, text, regex).first()
}

inline fun <reified T : Any> instanceFromRegex(
    text: String,
    @Language("RegExp") pattern: String,
    noinline buildAction: RegexMatcher.Builder.() -> Unit = {}
): T {
    return instancesFromRegex(T::class, text, regex(pattern, buildAction)).first()
}
