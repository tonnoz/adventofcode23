package com.tonnoz.adventofcode23.one

import org.springframework.core.io.ClassPathResource

val digitWords = mapOf(
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

val digitWordsR = mapOf(
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
fun String.readFileAsLinesUsingUseLines(): List<String> = ClassPathResource(this).file.useLines { it.toList() }
fun String.firstDigit(): Char = this.first{ aChar -> aChar.isDigit() }
fun String.lastDigit(): Char = this.last{ aChar -> aChar.isDigit() }

fun String.firstDigitOrDigitWordWindowed(): Int {
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

fun String.lastDigitOrDigitWordWindowed(): Int {
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

fun String.firstDigitOrDigitWordNaive(): Int {
  for (i in indices) {
    if (this[i].isDigit()) return this[i].digitToInt()
    if (this[i] == 'o' && this[i + 1] == 'n' && this[i + 2] == 'e') {
      return 1
    }
    if (this[i] == 't' && this[i + 1] == 'w' && this[i + 2] == 'o') {
      return 2
    }
    if(this[i] == 't' && this[i + 1] == 'h' && this[i + 2] == 'r' && this[i + 3] == 'e' && this[i + 4] == 'e') {
      return 3
    }
    if(this[i] == 'f' && this[i + 1] == 'o' && this[i + 2] == 'u' && this[i + 3] == 'r') {
      return 4
    }
    if(this[i] == 'f' && this[i + 1] == 'i' && this[i + 2] == 'v' && this[i + 3] == 'e') {
      return 5
    }
    if(this[i] == 's' && this[i + 1] == 'i' && this[i + 2] == 'x') {
      return 6
    }
    if(this[i] == 's' && this[i + 1] == 'e' && this[i + 2] == 'v' && this[i + 3] == 'e' && this[i + 4] == 'n') {
      return 7
    }
    if(this[i] == 'e' && this[i + 1] == 'i' && this[i + 2] == 'g' && this[i + 3] == 'h' && this[i + 4] == 't') {
      return 8
    }
    if(this[i] == 'n' && this[i + 1] == 'i' && this[i + 2] == 'n' && this[i + 3] == 'e') {
      return 9
    }
  }
  return -9999999
}

fun String.lastDigitOrDigitWordNaive(): Int {
  for (i in this.indices.reversed()) {
    if (this[i].isDigit()) return this[i].digitToInt()
    if (this[i] == 'e' && this[i - 1] == 'n' && this[i - 2] == 'o') {
      return 1
    }
    if (this[i] == 'o' && this[i - 1] == 'w' && this[i - 2] == 't') {
      return 2
    }
    if(this[i] == 'e' && this[i - 1] == 'e' && this[i - 2] == 'r' && this[i - 3] == 'h' && this[i - 4] == 't') {
      return 3
    }
    if(this[i] == 'r' && this[i - 1] == 'u' && this[i - 2] == 'o' && this[i - 3] == 'f') {
      return 4
    }
    if(this[i] == 'e' && this[i - 1] == 'v' && this[i - 2] == 'i' && this[i - 3] == 'f') {
      return 5
    }
    if(this[i] == 'x' && this[i - 1] == 'i' && this[i - 2] == 's') {
      return 6
    }
    if(this[i] == 'n' && this[i - 1] == 'e' && this[i - 2] == 'v' && this[i - 3] == 'e' && this[i - 4] == 's') {
      return 7
    }
    if(this[i] == 't' && this[i - 1] == 'h' && this[i - 2] == 'g' && this[i - 3] == 'i' && this[i - 4] == 'e') {
      return 8
    }
    if(this[i] == 'e' && this[i - 1] == 'n' && this[i - 2] == 'i' && this[i - 3] == 'n') {
      return 9
    }
  }
  return -9999999
}

fun main(args: Array<String>) {
  val inputLines = "input.txt".readFileAsLinesUsingUseLines()

  inputLines
    .sumOf { "${it.firstDigit()}${it.lastDigit()}".toInt() }
    .let { println("solution 1st q: $it") } //solution first problem: 55108

  inputLines
    .sumOf { "${it.firstDigitOrDigitWordWindowed()}${it.lastDigitOrDigitWordWindowed()}".toInt() }
    .let { println("solution 2nd q: $it") } //solution second problem: 56324

}
