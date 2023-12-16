package com.tonnoz.adventofcode23.day13

import com.tonnoz.adventofcode23.utils.*
import kotlin.math.min
import kotlin.system.measureTimeMillis

object Day13 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input13.txt".readInputSpaceDelimited()
    val time = measureTimeMillis {
      input.map { MirrorPath(it) }
        .sumOf { it.toMirrorPathExitSmudged() } //use .toMirrorPathExit() to solve for part 1
        .let { println("P2: $it") }
    }
    println("P2 time: $time ms")
  }

  data class MirrorPath(val mirrorGrid : List<String>)

  private fun MirrorPath.toMirrorPathExit(): Long = verticalMirrorIndex() + horizontalMirrorIndex() * 100

  private fun MirrorPath.verticalMirrorIndex(previous: Int = -1): Long {
    for (i in 1 until mirrorGrid[0].length) {
      if (i == previous) continue //do not check the previous column, if provided
      val minCols = min(i, mirrorGrid[0].length - i)
      var match = true
      for (r in 1..minCols) {
        if (mirrorGrid.map { it[i - r] } != mirrorGrid.map { it[i + r - 1] }) {
          match = false
          break
        }
      }
      if (match) return i.toLong()
    }
    return 0L
  }

  private fun MirrorPath.horizontalMirrorIndex(mir: Int = -1): Long {
    for (i in 1 until mirrorGrid.size) {
      if(i == mir) continue //do not check the previous row, if provided
      val minRows = min(i, mirrorGrid.size - i)
      var match = true
      for (r in 1..minRows) {
        if (mirrorGrid[i - r] != mirrorGrid[i + r - 1]) {
          match = false
          break
        }
      }
      if (match) return i.toLong()
    }
    return 0L
  }

  private fun MirrorPath.toMirrorPathExitSmudged(): Long {
    val initialVertical = this.verticalMirrorIndex()
    val initialHorizontal = this.horizontalMirrorIndex()
    val newGrid = mirrorGrid.map { it.toMutableList() }.toMutableList()
    for (y in mirrorGrid.indices) {
      for (x in mirrorGrid[y].indices) {
        invertElementInGrid(newGrid, y, x)
        val newMirrorPath = MirrorPath(newGrid.map { it.joinToString("") })
        val newVertical = newMirrorPath.verticalMirrorIndex(initialVertical.toInt())
        if (newVertical > 0) return newVertical //skip horizontal and all next inverted grids if vertical is found
        val newHorizontal = newMirrorPath.horizontalMirrorIndex(initialHorizontal.toInt())
        if (newHorizontal > 0) return newHorizontal * 100
        invertElementInGrid(newGrid, y, x)
      }
    }
    throw IllegalStateException("Not possible")
  }

  private fun invertElementInGrid(newGrid: MutableList<MutableList<Char>>, y: Int, x: Int) {
    when (newGrid[y][x]) {
      '.' -> newGrid[y][x] = '#'
      '#' -> newGrid[y][x] = '.'
    }
  }
}