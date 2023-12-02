package utils

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

annotation class RegexParsable

data class RegexMatcher(val regex: Regex, val subMatchers: Map<Int, RegexMatcher>) {

    class Builder(private val regex: Regex) {
        private var subMatchers: MutableMap<Int, RegexMatcher> = mutableMapOf()

        infix fun RegexMatcher.forGroup(index: Int) {
            this@Builder.subMatchers[index] = this
        }

        fun build(): RegexMatcher = RegexMatcher(regex = regex, subMatchers = subMatchers.toMap())

    }
}

fun regex(regex: Regex, buildAction: RegexMatcher.Builder.() -> Unit = {}): RegexMatcher {
    return RegexMatcher.Builder(regex).apply(buildAction).build()
}


@Suppress("UNCHECKED_CAST")
fun <T : Any> instancesFromRegex(
    clazz: KClass<T>,
    text: String,
    regex: RegexMatcher,
): List<T> {
    when (clazz) {
        Int::class -> return listOf(text.toInt() as T)
        String::class -> return regex.regex.findAll(text)
            .flatMap { it.groupValues.subList(1, it.groupValues.size) }.map { it as T }.toList()
        Boolean::class -> return listOf(text.toBoolean() as T)
    }
    val constructor = clazz.primaryConstructor!!
    val typeTransform = { idx: Int, match: String ->
        val param = constructor.parameters[idx]
        val type = param.type
        when (val erasure = type.jvmErasure) {
            Int::class -> match.toInt()
            Boolean::class -> match.toBoolean()
            String::class -> match
            else -> when {
                type.isSubtypeOf(Enum::class.starProjectedType) -> enumFromString(erasure as KClass<out Enum<*>>, match)
                type.isSubtypeOf(List::class.starProjectedType) -> {
                    val listType = type.arguments.first().type!!.jvmErasure
                    instancesFromRegex(listType, match, regex.subMatchers[idx]!!)
                }

                type.hasAnnotation<RegexParsable>() -> {
                    instanceFromRegex(match, regex.subMatchers[idx]!!)
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
    regex: Regex,
    noinline buildAction: RegexMatcher.Builder.() -> Unit = {}
): T {
    return instancesFromRegex(T::class, text, regex(regex, buildAction)).first()
}
