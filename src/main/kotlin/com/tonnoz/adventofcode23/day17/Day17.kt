package com.tonnoz.adventofcode23.day17

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.toListOfInts
import java.util.*
import kotlin.system.measureTimeMillis

object Day17 {
  @JvmStatic
  fun main(args: Array<String>) {
    val city = "input17.txt".readInput().toListOfInts()
    val timer1 = measureTimeMillis { calculateShortestPath(city, ::getCandidatesPt1).println() }
    "Part 1: $timer1 ms\n".println()
    val timer2 = measureTimeMillis { calculateShortestPath(city, ::getCandidatesPt2).println() }
    "Part 2: $timer2 ms\n".println()
  }

  enum class Direction {
   UP, DOWN, LEFT, RIGHT;
    fun toChar() = when (this) {
      UP -> '↑'
      DOWN -> '↓'
      LEFT -> '←'
      RIGHT -> '→'
    }
  }

  data class Block(val row: Int, val col: Int, var dir: Direction? = null, var minHeatLoss: Int = Int.MAX_VALUE) {
    override fun equals(other: Any?): Boolean {
      return when {
        other == null ->  false
        other !is Block ->  false
        this === other ->  true
        row != other.row ->  false
        col != other.col ->  false
        dir != other.dir ->  false
        else -> true
      }
    }
    override fun hashCode(): Int = 31 * row + col
  }

  private fun calculateShortestPath(city: List<List<Int>>, getCandidates: (Block, List<List<Int>>) -> List<Block>): Int {
    val visited = mutableMapOf<Block, Int>()
    val pq = PriorityQueue<Block>(compareBy { it.minHeatLoss })
    val start = Block(0, 0, minHeatLoss =  0)
    val end = Block(city.size - 1, city[0].size - 1)
    pq.add(start)
    while (pq.isNotEmpty()) {
      val cur = pq.poll()
      if (isExit(cur, city)) break
      getCandidates(cur, city).filter { block ->
        val currentHeatLoss = visited.getOrDefault(block, Int.MAX_VALUE)
        currentHeatLoss > block.minHeatLoss
      }.forEach { block ->
        visited[block] = block.minHeatLoss
        pq.add(block)
      }
    }
    return Direction.entries.minOf { visited.getOrDefault(end.copy(dir =  it), Int.MAX_VALUE) }
  }

  private fun isExit(block: Block, city: List<List<Int>>) = block.col == city[0].size && block.row == city.size
  private fun getCandidatesPt1(block: Block, city: List<List<Int>>) = getCandidates(block, city, 1..3)
  private fun getCandidatesPt2(block: Block, city: List<List<Int>>) = getCandidates(block, city, 1..10)

  private fun getCandidates(block: Block, city: List<List<Int>>, range: IntRange): List<Block> {
    return buildList {
      when (block.dir) {
        Direction.LEFT, Direction.RIGHT, null -> { //when null just pick a direction (left or right)
          var shortestUP = block.minHeatLoss
          var shortestDOWN = block.minHeatLoss
          for (s in range) {
            shortestUP += city.getOrNull(block.row - s)?.getOrNull(block.col) ?: 0
            if (range.last == 3) add(Block(block.row - s, block.col, Direction.UP, shortestUP))
            shortestDOWN += city.getOrNull(block.row + s)?.getOrNull(block.col) ?: 0
            if (range.last == 3) add(Block(block.row + s, block.col, Direction.DOWN, shortestDOWN))
            if (s >= 4) {
              add(Block(block.row - s, block.col, Direction.UP, shortestUP))
              add(Block(block.row + s, block.col, Direction.DOWN, shortestDOWN))
            }
          }
        }
        Direction.UP, Direction.DOWN -> {
          var shortestLEFT = block.minHeatLoss
          var shortestRIGHT = block.minHeatLoss
          for (s in range) {
            shortestLEFT += city.getOrNull(block.row)?.getOrNull(block.col - s) ?: 0
            if (range.last == 3) add(Block(block.row, block.col - s, Direction.LEFT, shortestLEFT))
            shortestRIGHT += city.getOrNull(block.row)?.getOrNull(block.col + s) ?: 0
            if (range.last == 3) add(Block(block.row, block.col + s, Direction.RIGHT, shortestRIGHT))
            if (s >= 4) {
              add(Block(block.row, block.col - s, Direction.LEFT, shortestLEFT))
              add(Block(block.row, block.col + s, Direction.RIGHT, shortestRIGHT))
            }
          }
        }
      }
    }.filter { it.col in city[0].indices && it.row in city.indices }
  }
}