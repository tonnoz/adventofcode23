package com.tonnoz.adventofcode23.day18

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.math.abs
import kotlin.system.measureTimeMillis

object Day18 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input18.txt".readInput()
    println("time part1: ${measureTimeMillis { calculateArea(input) { it.toDrillInstroPt1() }.println() }}ms")
    println("time part2: ${measureTimeMillis { calculateArea(input) { it.toDrillInstroPt2() }.println() }}ms")
  }

  private fun calculateArea(input: List<String>, transformF: (String) -> DrillingInstruction): Long {
    val (trenchPerimeter, trench) = input.map(transformF).toTrenchVertices()
    return trench.shoelaceArea(trenchPerimeter)
  }

  data class DrillingInstruction(val direction: Direction, val distance: Long, val color: String)

  enum class Direction(val char: Char) {
    UP('U'), DOWN('D'), LEFT('L'), RIGHT('R');
    companion object {
      private val map = entries.associateBy(Direction::char)
      fun fromChar(char: Char): Direction = map[char]!!
      fun fromChar2(char: Char) = when (char) {
        '0' -> RIGHT
        '1' -> DOWN
        '2' -> LEFT
        '3' -> UP
        else -> throw IllegalArgumentException("Unknown Direction")
      }
    }
  }

  private fun String.toDrillInstroPt1(): DrillingInstruction {
    val direction = Direction.fromChar(this.first())
    val distance = this.substring(2,4).trim().toLong()
    val color = this.takeLast(8).take(7)
    return DrillingInstruction(direction, distance, color)
  }

  private fun String.toDrillInstroPt2(): DrillingInstruction {
    val color = this.takeLast(8).take(7)
    val distance = color.substring(1, color.length-1).toLong(radix = 16)
    val direction = Direction.fromChar2(color.last())
    return DrillingInstruction(direction, distance, color)
  }

  private fun List<DrillingInstruction>.toTrenchVertices(): Pair<Long, List<Pair<Long, Long>>> {
    var curRow = 0L
    var curCol = 0L
    var perimeter = 0L
    this.map {
      when (it.direction) {
        Direction.UP -> curRow -= it.distance
        Direction.DOWN -> curRow += it.distance
        Direction.LEFT -> curCol -= it.distance
        Direction.RIGHT -> curCol += it.distance
      }
      perimeter += it.distance
      Pair(curRow, curCol)
    }.let { return Pair(perimeter, it) }
  }

  /**
   * Calculate the area of the trench polygon using the shoelace formula.
   * This is a modified version that includes the perimeter of the polygon.
   * https://en.wikipedia.org/wiki/Shoelace_formula
   * https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area#:~:text=x%2C%20y)%20%3D%2030.0-,Kotlin,-//%20version%201.1.3%0A%0Aclass
   */
  private fun List<Pair<Long,Long>>.shoelaceArea(perimeter: Long): Long {
    val n = this.size
    var result = 0.0
    for (i in 0 until n - 1) {
      result += this[i].first * this[i + 1].second - this[i + 1].first * this[i].second
    }
    return (((abs(result + this[n - 1].first * this[0].second - this[0].first * this[n -1].second) + perimeter) / 2) + 1).toLong()
  }
}