package com.tonnoz.adventofcode23.day5

import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

object Five {

  @JvmStatic
  fun main(args: Array<String>) {
    val time = measureTimeMillis {
      val input = "inputFive.txt".readInput()
      val seeds = input[0].parseSeeds()
      // I like explicit names :)
      val seedToSoilMap = input.parseRanges("seed-to-soil map:", "")
      val soilToFertilizerMap = input.parseRanges("soil-to-fertilizer map:", "")
      val fertilizerToWaterMap = input.parseRanges("fertilizer-to-water map:", "")
      val waterToLightMap = input.parseRanges("water-to-light map:", "")
      val lightToTemperatureMap = input.parseRanges("light-to-temperature map:", "")
      val temperatureToHumidityMap = input.parseRanges("temperature-to-humidity map:", "")
      val humidityToLocationMap = input.parseRanges("humidity-to-location map:", "")
      seeds.asSequence()
        .map { checkSeedAgainstRanges(it, seedToSoilMap) }
        .map { checkSeedAgainstRanges(it, soilToFertilizerMap) }
        .map { checkSeedAgainstRanges(it, fertilizerToWaterMap) }
        .map { checkSeedAgainstRanges(it, waterToLightMap) }
        .map { checkSeedAgainstRanges(it, lightToTemperatureMap) }
        .map { checkSeedAgainstRanges(it, temperatureToHumidityMap) }
        .map { checkSeedAgainstRanges(it, humidityToLocationMap) }
        .min()
        .let { println("lowest location: $it") }
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