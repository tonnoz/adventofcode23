package com.tonnoz.adventofcode23.day5

import com.tonnoz.adventofcode23.utils.readInput
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking

object FiveTwo { //leave an extra empty line in the input file!
  @OptIn(FlowPreview::class)
  @JvmStatic
  fun main(args: Array<String>) {
    val time = System.currentTimeMillis()
    val input = "input5.txt".readInput()
    val seeds = input[0].parseSeeds().parseRangesPart2() //comment out .parseRangesPart2() to solve part 1
    val maps = listOf(
      "seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:",
      "water-to-light map:", "light-to-temperature map:", "temperature-to-humidity map:",
      "humidity-to-location map:"
    ).map { key -> input.parseRanges(key, "") }

    runBlocking { //runBlocking is needed in order to use flow (coroutines)
      seeds
        .asFlow() // emit each Long lazily
        .flatMapMerge { it.asFlow() } // use flatMapMerge for parallel processing
        .map { maps.fold(it) { acc, curr -> acc.mapSeedToFirstMatchingRange(curr) } }
        .reduce { acc, curr -> minOf(acc, curr) }
        .let {  println("lowest location: $it \nmilliseconds elapsed: ${System.currentTimeMillis() - time}") }
    }
  }

  private data class Range(val sourceRange: LongRange, val destinationRange: LongRange)
  private fun List<Long>.parseRangesPart2() : List<LongRange> = this.windowed(2,2).map {it.first()..<it.first() + it.last() }
  private fun String.parseSeeds() : List<Long> = "\\d+".toRegex().findAll(this).map { it.value.toLong() }.toList()
  private fun Long.mapSeedToFirstMatchingRange(ranges: List<Range>): Long {
    ranges.asSequence().forEach {
      if(this < it.sourceRange.first) return@forEach //return@forEach is like a "continue" in a for loop
      if(this > it.sourceRange.last) return@forEach
      return this.mapRange(it)
    }
    return this //return seed if no range matches
  }
  private fun Long.mapRange(range: Range): Long = range.sourceRange.positionInTheRange(this) + range.destinationRange.first
  private fun LongRange.positionInTheRange(number: Long): Long = number - this.first
  private fun List<String>.parseRanges(startLine:String, endLine:String): List<Range> {
    val startI = this.indexOfFirst { it.startsWith(startLine) } + 1
    val endI = this.subList(startI+1, this.size).indexOfFirst { it == endLine } + startI + 1
    return this.subList(startI, endI).map {
      val (destination,source, rangeLength ) = it.split(" ", limit = 3)
      val sourceRange = source.toLong()..< source.toLong() + rangeLength.toLong()
      val destinationRange = destination.toLong()..< destination.toLong() + rangeLength.toLong()
      Range(sourceRange, destinationRange)
    }
  }
}