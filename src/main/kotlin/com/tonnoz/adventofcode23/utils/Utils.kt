package com.tonnoz.adventofcode23.utils

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun String.readInput() = File("src/main/resources", this).readLines()

fun List<String>.toCharMatrix() = ArrayList<CharArray>(this.map { it.toCharArray() })

/**
 * Splits into space-separate parts of input and maps each part.
 */

fun <T> T.println(): T = also { println(this) }

fun <R> List<String>.parts(map: (List<String>) -> R): List<R> = buildList {
  var cur = ArrayList<String>()
  for (s in this@parts) {
    if (s == "") {
      add(map(cur))
      cur = ArrayList()
      continue
    }
    cur.add(s)
  }
  if (cur.isNotEmpty()) add(map(cur))
}

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