package com.tonnoz.adventofcode23.six

import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.transpose
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

object Six {
  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputSix.txt".readInput()
    problemOne(input.parseRaces())
    problemTwo(input.parseRaces2())
  }

  private fun problemTwo(input: List<Long>){
    val time = System.currentTimeMillis()
    waysToWinConstantTime(input[0], input[1])
      .also { println(it) }
    println("Time p2: ${System.currentTimeMillis() - time} ms")
  }

  private fun problemOne(input: List<List<Long>>){
    val time = System.currentTimeMillis()
    input
      .map { findAllBestHoldTimes(it[0], it[1]) }
      .fold(1) { acc, curr -> acc * curr }
      .also { println(it) }
    println("Time p1: ${System.currentTimeMillis() - time} ms")
  }

  private fun List<String>.parseRaces() = this.map{"\\d+".toRegex().findAll(it).map { it.value.toLong() }.toList()}.transpose()
  private fun List<String>.parseRaces2() = this.map{"\\d+".toRegex().findAll(it).map { it.value}.joinToString(separator = "").toLong()}

  private fun findAllBestHoldTimes(raceTime: Long, recordTime: Long): Int {
    val possibleHoldTimes = mutableListOf<Long>()
    for (holdTime in 0..raceTime) {
      val distance = holdTime * (raceTime - holdTime)
      if (distance > recordTime) {
        possibleHoldTimes.add(holdTime)
      }
    }
    return possibleHoldTimes.size
  }

  private fun waysToWinConstantTime(time: Long, minDistance: Long): Long { //by Tim Stockman: https://github.com/timstokman
    val sqrt = sqrt(time * time - 4 * minDistance.toDouble())
    var minPressed = floor(0.5 * (time - sqrt)).toLong()
    minPressed += if ((time - minPressed) * minPressed > minDistance) 0 else 1
    var maxPressed = floor(0.5 * (sqrt + time)).toLong()
    maxPressed += if ((time - maxPressed) * maxPressed > minDistance) 1 else 0
    return max(0, maxPressed - minPressed)
  }

  private fun Int.bestHoldTimeForRace(): Int { // formula parabola
    val optimalHoldTime = (this / 2.0).toInt()
    return optimalHoldTime * (this - optimalHoldTime)
  }
}