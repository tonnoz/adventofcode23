package com.tonnoz.adventofcode23.utils

import com.tonnoz.adventofcode23.day17.Day17
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun List<String>.toListOfInts() = this.map { row -> row.map { col -> col.toString().toInt() } }


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

//private fun List<List<Day17.Block>>.printCity(current: Day17.Block? = null, visited: MutableMap<Day17.Block, Int>) = this
//  .forEach { row ->
//    row.forEach {
//      when{
//        it == current -> print("[${it.dir?.toChar()?.bold("95")}]")
//        visited.contains(it) -> print("[${it.minHeatLoss.bold("93")}]")
//        else -> print("[${if(it.minHeatLoss  == Int.MAX_VALUE) "X" else  it.minHeatLoss}]")
//      }
//    }
//    println("")
//  }.also { println("") }
//
//private fun List<List<Day17.Block>>.printVisited(current: MutableList<Day17.Block>, visited: MutableMap<Day17.Block, Int>) = this
//  .forEach { row ->
//    row.forEach {
//      when{
//          it.col == 0 && it.row == 0 -> print("[${"S".bold("91")}${it.minHeatLoss.toChar().bold("93")}]")
//        it in current -> print("[${it.minHeatLoss.bold("91")}${it.minHeatLoss.toChar().bold("93")}]")
//        visited.contains(it) -> print("[${it.minHeatLoss.bold("95")}${it.minHeatLoss.toChar().bold("93")}]")
//        else -> print("[${if(it.minHeatLoss == Int.MAX_VALUE) "X" else  it.minHeatLoss} ]")
//      }
//    }
//    println("")
//  }.also { println("") }
//
//private fun List<List<Day17.Block>>.printSolutionPath(current: MutableList<Day17.Block>) = this
//  .forEach { row ->
//    row.forEach {
//      when{
//        it in current -> print("[${it.minHeatLoss.bold("91")}${it.minHeatLoss.toChar().bold("93")}]")
//        else -> print("[${if(it.minHeatLoss == Int.MAX_VALUE) "X" else  it.minHeatLoss} ]")
//      }
//    }
//    println("")
//  }.also { println("") }