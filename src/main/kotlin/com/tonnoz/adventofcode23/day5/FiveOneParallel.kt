package com.tonnoz.adventofcode23.day5

import com.tonnoz.adventofcode23.utils.readInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

object FiveOneParallel {

  @JvmStatic
  fun main(args: Array<String>) {
    val time = measureTimeMillis {
      val input = "input5.txt".readInput()
      val seeds = input[0].parseSeeds()
      val maps = listOf(
        "seed-to-soil map:",
        "soil-to-fertilizer map:",
        "fertilizer-to-water map:",
        "water-to-light map:",
        "light-to-temperature map:",
        "temperature-to-humidity map:",
        "humidity-to-location map:"
      ).map { key -> input.parseRanges(key, "") }
      val lowestLocation = runBlocking { // Use coroutines for parallel processing
        seeds.map { seed ->
          async(Dispatchers.Default) { //Dispatchers.Default is the best for CPU intensive tasks
            maps.fold(seed) { acc, map -> checkSeedAgainstRanges(acc, map) }
          }
        }.minOf { it.await() }
      }
      println("lowest location: $lowestLocation")
    }
    println("milliseconds elapsed: $time")
  }

  private data class Range(val sourceRange: LongRange, val destinationRange: LongRange)

  private fun String.parseSeeds() : List<Long> = "\\d+".toRegex().findAll(this).map { it.value.toLong() }.toList()

  private fun checkSeedAgainstRanges(seed: Long, ranges: List<Range>): Long {
    ranges.forEach {
      val maybeSeed = transform(seed, it)
      if(maybeSeed != null) return maybeSeed
    }
    return seed
  }

  private fun transform(seed: Long, range: Range): Long? = range
    .sourceRange
    .positionInTheRange(seed)
    ?.let { range.destinationRange.elementAt(it.toInt()) }


  private fun LongRange.positionInTheRange(number: Long): Long? = if (number in this) number - this.first else null

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