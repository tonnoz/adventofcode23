package com.tonnoz.adventofcode23.day14

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.toCharMatrix
import kotlin.system.measureTimeMillis

private const val ROCK = 'O'
private const val MOUNTAIN = '#'
private const val EMPTY = '.'

object Day14Part2 {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input14.txt".readInput().toCharMatrix()
    val time = measureTimeMillis {
      input.tilt4Directions(times = 1_000_000_000)
        .calculateLoad(input.size)
        .println()
    }
    println("P2 time: $time ms")
  }

  private fun MutableList<CharArray>.tilt4Directions(times: Int): MutableList<CharArray> {
    val seen = mutableMapOf<Int, Int>()
    var cycleNumber = 1
    while (cycleNumber <= times) {
      tiltGrid()
      when (val state = sumOf { it.joinToString("").hashCode() }) {
        !in seen -> seen[state] = cycleNumber++
        else -> return tiltUntilSeenAgain(cycleNumber, seen, state, times)
      }
    }
    return this
  }

  private fun MutableList<CharArray>.tiltUntilSeenAgain(cycleNumber: Int, seen: MutableMap<Int, Int>, state: Int, times: Int): MutableList<CharArray> {
    val cycleLength = cycleNumber - seen[state]!!
    val cyclesRemaining = (times - cycleNumber) % cycleLength
    repeat(cyclesRemaining) { tiltGrid() }
    return this
  }

  private fun MutableList<CharArray>.tiltGrid() {
    doForAllElements(::moveRockRecursive, "N")
    doForAllElements(::moveRockRecursive, "W")
    doForAllElements(::moveRockRecursive, "S")
    doForAllElements(::moveRockRecursive, "E")
  }

  private fun MutableList<CharArray>.doForAllElements(aFunction: (MutableList<CharArray>, Pair<Int, Int>, String) -> Unit, direction:String) {
    for (i in indices) {
      for(j in this[i].indices) {
        aFunction(this, Pair(i, j), direction)
      }
    }
  }

  private fun moveRockRecursive(mountain: MutableList<CharArray>, from:Pair<Int,Int>, direction:String) {
    if(mountain[from.first][from.second] != ROCK) return
    return moveRockRecursiveGeneric(mountain, from,  getRockDirection(direction, from.first, from.second), direction)
  }

  private tailrec fun moveRockRecursiveGeneric(mountain: MutableList<CharArray>, from:Pair<Int,Int>, to: Pair<Int,Int>, direction:String) {
    fun outOfBound() = to.first < 0 || to.second < 0 || to.first >= mountain.size || to.second >= mountain.size

    if (outOfBound() || mountain[to.first][to.second] == MOUNTAIN) return
    if (mountain[to.first][to.second] == EMPTY) {
      swap(mountain, to, from)
      moveRockRecursiveGeneric(mountain, to, getRockDirection(direction, to.first, to.second), direction)
    } else {
      moveRockRecursiveGeneric(mountain, from, getRockDirection(direction, to.first, to.second), direction)
    }
  }

  private fun swap(mountain: MutableList<CharArray>, to: Pair<Int, Int>, from: Pair<Int, Int>) {
    mountain[to.first][to.second] = ROCK
    mountain[from.first][from.second] = EMPTY
  }

  private fun getRockDirection(direction:String, i:Int, j:Int)=
    when(direction){
      "N" -> Pair(i - 1, j)
      "S" -> Pair(i + 1, j)
      "E" ->  Pair(i, j + 1)
      "W" ->  Pair(i, j-1)
      else -> throw IllegalArgumentException("Unknown direction $direction")
    }

  private fun MutableList<CharArray>.calculateLoad(size: Int) =
    foldIndexed(0L) { i, acc, curr -> acc + curr.countRocks() * (size - i)  }

  private fun CharArray.countRocks(): Int = count { it == ROCK }
}