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
        .sumOf { it.toMirrorPathExit() }
        .println()
    }
    println("P1 time: $time ms")
  }

  data class MirrorPath(val mirrorGrid : List<String>)

  private fun MirrorPath.toMirrorPathExit(): Long = verticalMirrorIndex() + horizontalMirrorIndex() * 100

  private fun MirrorPath.verticalMirrorIndex(): Long {
    for (i in 1 until mirrorGrid[0].length) {
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

  private fun MirrorPath.horizontalMirrorIndex(): Long {
    for (i in 1 until mirrorGrid.size) {
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
}