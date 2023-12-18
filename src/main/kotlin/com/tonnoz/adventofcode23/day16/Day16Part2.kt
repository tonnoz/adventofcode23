package com.tonnoz.adventofcode23.day16

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readCharMatrix
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

object Day16Part2 {

  @JvmStatic
  fun main(args: Array<String>) = runBlocking {
    val input = "input16.txt".readCharMatrix().toMutableList()
    val time = measureTimeMillis {
      val contraption = input.mapIndexed{i, row -> row.mapIndexed{j, value -> Cell(i, j, value)}}
      val memory = mutableMapOf<Cell,List<Direction>>()
      val solutions = mutableListOf<Int>()
      val topEdgeCells = contraption.first().drop(1).dropLast(1)
      val bottomEdgeCells = contraption.last().drop(1).dropLast(1)
      val leftEdgeCells = contraption.map { it.first() }
      val rightEdgeCells = contraption.map { it.last() }
      contraption.countEnergizedCells(topEdgeCells, Direction.DOWN, memory, solutions)
      contraption.countEnergizedCells(bottomEdgeCells, Direction.UP, memory, solutions)
      contraption.countEnergizedCells(leftEdgeCells, Direction.RIGHT, memory, solutions)
      contraption.countEnergizedCells(rightEdgeCells, Direction.LEFT, memory, solutions)
      solutions.max().println()
    }
    println("P2 time: $time ms")
  }

  private fun List<List<Cell>>.countEnergizedCells(edgeCells: List<Cell>, direction: Direction, memory: MutableMap<Cell, List<Direction>>, solutions: MutableList<Int>) {
    edgeCells.forEach { cell ->
      go(cell, this, memory, direction)
      val max = this.sumOf { row -> row.count { it.energized } }
      solutions.add(max)
      this.forEach { row -> row.forEach { it.energized = false } }
      memory.clear()
    }
  }

  data class Cell(val row: Int, val column: Int, val value: Char, var energized: Boolean = false) {

    fun nextCell(direction: Direction, contraption: List<List<Cell>>): Cell? {
      val cHeight = contraption.size
      val cWidth = contraption[0].size
      return when (direction) {
        Direction.UP -> if(row > 0) contraption[row-1][column] else null
        Direction.DOWN -> if(row < cHeight -1) contraption[row+1][column] else null
        Direction.LEFT -> if(column > 0) contraption[row][column-1] else null
        Direction.RIGHT -> if(column < cWidth -1) contraption[row][column+1] else null
      }
    }

    fun processSplit(direction: Direction) =
      when (value) {
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
        else -> { throw IllegalArgumentException("Invalid split cell: $this")}
      }

    fun processAngle(direction: Direction) =
      listOf(when (value) {
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
        else -> { throw IllegalArgumentException("Invalid angle cell: $this")}
      })

    fun processStraight(direction: Direction)= listOf(direction)
  }

  enum class Direction { UP, DOWN, LEFT, RIGHT }

  private fun go(cell: Cell, contraption: List<List<Cell>>, memory: MutableMap<Cell, List<Direction>>, direction: Direction) {
    if(memory[cell]?.contains(direction) == true) return //skip if seen before
    cell.energized = true
    memory[cell] = memory[cell]?.plus(direction) ?: listOf(direction) //add it to seen
    val nextMoves = when (cell.value) {
      '.' ->  cell.processStraight(direction)
      '|' , '-' -> cell.processSplit(direction)
      '/','\\' -> cell.processAngle(direction)
      else -> throw IllegalArgumentException("Invalid cell: $cell")
    }
    nextMoves.forEach {
      cell.nextCell(it, contraption)?.let { nextCell ->
        go(nextCell, contraption, memory, it)
      }
    }
  }

}