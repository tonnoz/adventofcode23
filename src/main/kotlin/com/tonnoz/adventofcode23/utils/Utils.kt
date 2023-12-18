package com.tonnoz.adventofcode23.utils

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


fun String.readInput() = File("src/main/resources", this).readLines()

fun String.readCharMatrix() = this.readInput().toCharMatrix()

fun List<String>.toCharMatrix() = ArrayList<CharArray>(this.map { it.toCharArray() })

fun String.readInputSpaceDelimited(): List<List<String>> =
  File("src/main/resources", this)
    .readText()
    .replace("\r", "")
    .split("\n\n")
    .map { it.split("\n") }


/**
 * Reads the entire content of the given input txt file as a single String.
 */
fun String.readInputAsString() = File("src/main/resources", this).readText()

/**
 * Splits into space-separate parts of input and maps each part.
 */

fun <T> T.println(): T = also { println(this) }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun List<List<Long>>.transpose(): List<List<Long>> {
  if (this.isEmpty() || this.first().isEmpty()) return listOf()
  val cols = this.first().size
  val rows = this.size
  return List(cols) { j ->
    List(rows) { i ->
      this[i][j]
    }
  }
}