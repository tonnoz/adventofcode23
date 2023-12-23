package com.tonnoz.adventofcode23.day18

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.math.abs
import kotlin.system.measureTimeMillis

object Day18 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input18.txt".readInput()
    println("time part1: ${measureTimeMillis { part1(input).println() }}ms")
  }

  private fun part1(input: List<String>): Long {
    val digPlan = input.map { it.toDrillInstroPt1() }
    val (trenchPerimeter, trench) = digPlan.toTrenchVertices()
    return trench.shoelaceArea(trenchPerimeter)
  }

  data class DrillInstro(val direction: Direction, val distance: Int, val color: String)

  enum class Direction(val char: Char) {
    UP('U'), DOWN('D'), LEFT('L'), RIGHT('R');
    companion object {
      private val map = entries.associateBy(Direction::char)
      fun fromChar(char: Char): Direction = map[char]!!
    }
  }
  private fun String.toDrillInstroPt1(): DrillInstro {
    val direction = Direction.fromChar(this.first())
    val distance = this.substring(2,4).trim().toInt()
    val color = this.takeLast(8).take(7)
    return DrillInstro(direction, distance, color)
  }

  private fun List<DrillInstro>.toTrenchVertices(): Pair<Long, List<Pair<Int, Int>>> {
    var curRow = 0
    var curCol = 0
    var edgeSum = 0L
    this.map {
      when (it.direction) {
        Direction.UP -> curRow -= it.distance
        Direction.DOWN -> curRow += it.distance
        Direction.LEFT -> curCol -= it.distance
        Direction.RIGHT -> curCol += it.distance
      }
      edgeSum += it.distance
      Pair(curRow, curCol)
    }.let { return Pair(edgeSum, it) }
  }

  /**
   * Calculate the area of the trench polygon using the shoelace formula.
   * This is a modified version that includes the perimeter of the polygon.
   * https://en.wikipedia.org/wiki/Shoelace_formula
   * https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area#:~:text=x%2C%20y)%20%3D%2030.0-,Kotlin,-//%20version%201.1.3%0A%0Aclass
   */
  private fun List<Pair<Int,Int>>.shoelaceArea(perimeter: Long): Long {
    val n = this.size
    var result = 0.0
    for (i in 0 until n - 1) {
      result += this[i].first * this[i + 1].second - this[i + 1].first * this[i].second
    }
    return (((abs(result + this[n - 1].first * this[0].second - this[0].first * this[n -1].second) + perimeter) / 2) + 1).toLong()
  }


  private fun part2(input: List<String>): Long {
    TODO()
  }
}