package com.tonnoz.adventofcode23.day15

import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

object Day15 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input15.txt".readInput().first().split(",")
    val time = measureTimeMillis {
      input.sumOf {
        it.fold(0L) { acc, curr -> ((acc + curr.code) * 17) % 256 }
      }.let {
        println("Part1: $it")
      }
    }
    println("P1 time: $time ms")
  }
}