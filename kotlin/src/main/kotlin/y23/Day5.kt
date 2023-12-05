package y23

import utils.*

data object Day5 : Day<List<Day5.Input>> {

    data class Entry(val destStart: Long, val sourceStart: Long, val count: Long) {

        val last = sourceStart + count - 1
        val range = sourceStart..last
        operator fun contains(value: Long): Boolean {
            return value in range
        }

        fun map(value: Long): Long {
            return destStart + (value - sourceStart)
        }

        fun map(value: LongRange): LongRange {
            return map(value.first)..map(value.last)
        }
    }

    data class Input(
        val seed: List<Long>,
        val seedToSoil: List<Entry>,
        val soilToFertilizer: List<Entry>,
        val fertilizerToWater: List<Entry>,
        val waterToLight: List<Entry>,
        val lightToTemp: List<Entry>,
        val tempToHum: List<Entry>,
        val humToLoc: List<Entry>
    )

    override val day: Int = 5
    override val part1TestInput: String = """
        seeds: 79 14 55 13
        
        seed-to-soil map:
        50 98 2
        52 50 48
        
        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15
        
        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4
        
        water-to-light map:
        88 18 7
        18 25 70
        
        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13
        
        temperature-to-humidity map:
        0 69 1
        1 0 69
        
        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 35L
    override val part2Expected: Number = 46L

    override fun parseFile(file: List<String>): List<Input> {
        val groups = Regex("([\\d\\s,]{2,})").findAll(file.joinToString()).map { it.groupValues.last() }.toList()
        val input = Input(
            seed = instancesFromRegex(Long::class, groups[0], regex("([0-9]+)")),
            seedToSoil = instancesFromRegex(Entry::class, groups[1], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            soilToFertilizer = instancesFromRegex(Entry::class, groups[2], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            fertilizerToWater = instancesFromRegex(Entry::class, groups[3], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            waterToLight = instancesFromRegex(Entry::class, groups[4], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            lightToTemp = instancesFromRegex(Entry::class, groups[5], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            tempToHum = instancesFromRegex(Entry::class, groups[6], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
            humToLoc = instancesFromRegex(Entry::class, groups[7], regex("([0-9]+) ([0-9]+) ([0-9]+)")),
        )
        return listOf(input)
    }

    override fun part1(input: List<Input>): Number {
        return with(input.first()) {
            seed.minOf {
                it.map(seedToSoil)
                    .map(soilToFertilizer)
                    .map(fertilizerToWater)
                    .map(waterToLight)
                    .map(lightToTemp)
                    .map(tempToHum)
                    .map(humToLoc)
            }
        }
    }

    override fun part2(input: List<Input>): Number {
        return with(input.first()) {
            seed.chunked(2) { it[0]..<it[0] + it[1] }
                .map(seedToSoil)
                .map(soilToFertilizer)
                .map(fertilizerToWater)
                .map(waterToLight)
                .map(lightToTemp)
                .map(tempToHum)
                .map(humToLoc)
                .minOf {
                    it.first
                }
        }
    }

    private fun List<LongRange>.map(entries: List<Entry>): List<LongRange> {
        return flatMap { range ->
            val result: MutableList<LongRange> = mutableListOf()
            var remaining: List<LongRange> = listOf(range)
            entries.forEach { entry ->
                remaining = remaining.flatMap {
                    when {
                        it.first in entry -> {
                            if (it.last in entry) {
                                result.add(entry.map(it))
                                listOf()
                            } else {
                                result.add(entry.map(it.first..entry.last))
                                listOf(entry.last + 1..it.last)
                            }
                        }

                        it.last in entry -> {
                            result.add(entry.map(entry.sourceStart..it.last))
                            listOf(it.first..<entry.sourceStart)
                        }

                        entry.sourceStart in it -> {
                            result.add(entry.map(entry.range))
                            listOf(it.first..<entry.sourceStart, entry.last + 1..it.last)
                        }

                        else -> {
                            listOf(it)
                        }
                    }
                }
            }
            (result + remaining)
        }
    }

    private fun Long.map(entries: List<Entry>): Long {
        return entries.fold(this) { acc, entry ->
            if (this in entry) {
                entry.map(this)
            } else {
                acc
            }
        }
    }

}