package com.tonnoz.adventofcode23.day16

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readCharMatrix
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

object Day16 {

  @JvmStatic
  fun main(args: Array<String>) = runBlocking {
    val input = "input16.txt".readCharMatrix().toMutableList()
    val time = measureTimeMillis {
      val contraption = input.mapIndexed{i, row -> row.mapIndexed{j, value -> Cell(i, j, value)}}
      val memory = mutableMapOf<Cell,List<Direction>>()
      contraption[0][0].energized = true
      go(contraption[0][0], contraption, memory, Direction.DOWN)
      contraption.flatMap { it.filter { it.energized } }.count().println()
    //      PRINT COLORED MATRIX :)
    //      contraption.forEach { row -> row.forEach { print(if(it.energized) it.value.bold("33") else it.value) }; println("") }
    }
    println("P1 time: $time ms")
  }

  data class Cell(val row: Int, val column: Int, val value: Char, var energized: Boolean = false) {

    fun nextCell(direction: Direction, contraption: List<List<Cell>>): Cell? {
      val contraptionHeight = contraption.size
      val contraptionWidth = contraption[0].size
      return when (direction) {
        Direction.UP -> if(row > 0) contraption[row-1][column] else null
        Direction.DOWN -> if(row < contraptionHeight -1) contraption[row+1][column] else null
        Direction.LEFT -> if(column > 0) contraption[row][column-1] else null
        Direction.RIGHT -> if(column < contraptionWidth -1) contraption[row][column+1] else null
      }
    }
  }

  enum class Direction {
     UP, DOWN, LEFT, RIGHT
  }

  private fun go(cell: Cell, contraption: List<List<Cell>>, memory: MutableMap<Cell, List<Direction>>, direction: Direction = Direction.LEFT) {
    val nextCell = cell.nextCell(direction, contraption) ?: return
    if(memory[nextCell]?.contains(direction) == true) return
    memory[nextCell] = memory[nextCell]?.plus(direction) ?: listOf(direction)
    nextCell.energized = true
    val nextMoves = when (nextCell.value) {
      '.' -> listOf(direction)
      '|' , '-' -> split(nextCell, direction)
      '/','\\' -> listOf(angle(nextCell, direction))
      else -> throw IllegalArgumentException("Invalid cell: $nextCell")
    }
    nextMoves.forEach { go(nextCell, contraption, memory, it) }
  }

  private fun split(cell: Cell, direction: Direction): List<Direction> {
    return when (cell.value) {
      '|' -> when (direction) {
        Direction.UP -> listOf(Direction.UP)
        Direction.DOWN -> listOf(Direction.DOWN)
        Direction.LEFT,Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
      }
      '-' -> when (direction) {
        Direction.UP, Direction.DOWN  -> listOf(Direction.LEFT, Direction.RIGHT)
        Direction.LEFT -> listOf(Direction.LEFT)
        Direction.RIGHT -> listOf(Direction.RIGHT)
      }
      else -> { throw IllegalArgumentException("Invalid split cell: $cell")}
    }
  }

  private fun angle(cell: Cell, direction: Direction): Direction {
    return when (cell.value) {
      '/' -> when (direction) {
        Direction.UP -> Direction.RIGHT
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.DOWN
        Direction.RIGHT -> Direction.UP
      }
      '\\' -> when (direction) {
        Direction.UP -> Direction.LEFT
        Direction.DOWN -> Direction.RIGHT
        Direction.LEFT -> Direction.UP
        Direction.RIGHT -> Direction.DOWN
      }
      else -> { throw IllegalArgumentException("Invalid angle cell: $cell")}
    }
  }

}