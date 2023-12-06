package com.tonnoz.adventofcode23.six

import com.tonnoz.adventofcode23.one.readFileAsAList
import com.tonnoz.adventofcode23.utils.transpose

object Six {
  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputSix.txt".readFileAsAList()
    problemOne(input.parseRaces())
    problemTwo(input.parseRaces2())
  }

  private fun problemTwo(input: List<Long>){
    val time = System.currentTimeMillis()
    findAllBestHoldTimes(input[0], input[1])
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

  private fun Int.bestHoldTimeForRace(): Int { // formula parabola
    val optimalHoldTime = (this / 2.0).toInt()
    return optimalHoldTime * (this - optimalHoldTime)
  }
}