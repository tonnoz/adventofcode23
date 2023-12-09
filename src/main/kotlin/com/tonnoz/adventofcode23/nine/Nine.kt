package com.tonnoz.adventofcode23.nine

import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

object Nine {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputNine.txt".readInput()
    problemOne(input)
    problemTwo(input)
  }

  private fun problemTwo(input: List<String>) {
    val time = measureTimeMillis {
      input.sumOf { aLine ->
        val numbers = aLine.split(" ").map { it.toLong() }
        buildNumbersSequenceLeft(numbers).map { it.first() }.sum()
      }.let { println(it) }
    }
    println("Time p2: $time ms")
  }

  private fun problemOne(input: List<String>) {
    val time = measureTimeMillis {
      input.sumOf { aLine ->
        val numbers = aLine.split(" ").map { it.toLong() }
        buildNumbersSequenceRight(numbers).map { it.last() }.sum()
      }.let { println(it) }
    }
    println("Time p1: $time ms")
  }

  private fun buildNumbersSequenceRight(numbers: List<Long>): Sequence<List<Long>> = numbers.buildNumbersSequences(::rtl)
  private fun buildNumbersSequenceLeft(numbers: List<Long>): Sequence<List<Long>> = numbers.buildNumbersSequences(::ltr)
  private fun List<Long>.buildNumbersSequences(windowedArg: (List<Long>) -> Long)= generateSequence(this) {
    nums -> nums.windowed(2) { windowedArg(it) }.takeIf { !it.all { num -> num == 0L } }
  }
  private fun ltr(longs: List<Long>) = longs[0] - longs[1]
  private fun rtl(longs: List<Long>) = longs[1] - longs[0]

  private fun problemOneIter(input:List<String>){
    val time = measureTimeMillis {
      input.sumOf { s ->
        var numbers = s.split(" ").map { it.toLong() }
        val zeros = ArrayList<Long>()
        do {
          zeros.add(numbers.last())
          numbers = numbers.windowed(2) { it[1] - it[0] }
        } while (!numbers.all { it == 0L })
        zeros.sum()
      }.let { println(it) }
    }
    println("Time p1: $time ms")
  }

}