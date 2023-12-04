package com.tonnoz.adventofcode23.one

import org.springframework.core.io.ClassPathResource
fun String.readFileAsAList(): List<String> = ClassPathResource(this).file.useLines { it.toList() }

object One{

  @JvmStatic
  fun main(args: Array<String>) {
    val inputLines = "inputOne.txt".readFileAsAList()

    inputLines
      .sumOf { "${it.firstDigit()}${it.lastDigit()}".toInt() }
      .let { println("solution 1st q: $it") } //solution first problem: 55108

    inputLines
      .sumOf { "${it.firstDigitOrDigitWord()}${it.lastDigitOrDigitWord()}".toInt() }
      .let { println("solution 2nd q: $it") } //solution second problem: 56324

  }

  private val digitWords = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
  )
  private val digitWordsR = mapOf(
    "eno" to 1,
    "owt" to 2,
    "eerht" to 3,
    "ruof" to 4,
    "evif" to 5,
    "xis" to 6,
    "neves" to 7,
    "thgie" to 8,
    "enin" to 9
  )
  private fun String.firstDigit(): Char = this.first{ aChar -> aChar.isDigit() }
  private fun String.lastDigit(): Char = this.last{ aChar -> aChar.isDigit() }

  private fun String.firstDigitOrDigitWord(): Int {
    for (i in indices) {
      if (this[i].isDigit()) return this[i].digitToInt()
      for ((word, value) in digitWords) {
        if (i + word.length <= length && substring(i, i + word.length) == word) {
          return value
        }
      }
    }
    return -9999999
  }

  private fun String.lastDigitOrDigitWord(): Int {
    for (i in this.indices.reversed()) {
      if (this[i].isDigit()) return this[i].digitToInt()
      for ((word, value) in digitWordsR) {
        if (i - word.length >= 0 && substring(i - word.length, i) == word) {
          return value
        }
      }
    }
    return -9999999
  }
}
