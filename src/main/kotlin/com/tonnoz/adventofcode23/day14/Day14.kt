package com.tonnoz.adventofcode23.day14

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.toCharMatrix
import kotlin.system.measureTimeMillis

object Day14 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input14.txt".readInput().toCharMatrix()
    val time = measureTimeMillis {
      input.rollAllRocksUpward()
        .foldIndexed(0L) { i, acc, curr -> acc + curr.countRocks() * (input.size - i)  }
        .println()
    }
    println("P1 time: $time ms")
  }

  private fun CharArray.countRocks(): Int = this.count { it == 'O' }

  private fun MutableList<CharArray>.rollAllRocksUpward(): MutableList<CharArray> {
    for (i in this.size-1 downTo  1) {
      this[i].forEachIndexed { j, rock ->
        if (rock == 'O') moveRockUpRecursive(this, Pair(i,j), Pair(i-1,j))
      }
    }
    return this
  }

  private tailrec fun moveRockUpRecursive(mountain: MutableList<CharArray>, from:Pair<Int,Int>, to: Pair<Int,Int>) {
    if(to.first < 0 || mountain[to.first][to.second] == '#') return
    if (mountain[to.first][to.second] == '.') {
      mountain[to.first][to.second] = 'O'
      mountain[from.first][from.second] = '.'
      moveRockUpRecursive(mountain, to, Pair(to.first-1, to.second))
    } else {
      moveRockUpRecursive(mountain, from, Pair(to.first-1, to.second))
    }
  }
}