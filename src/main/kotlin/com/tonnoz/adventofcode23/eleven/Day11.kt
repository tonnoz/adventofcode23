package com.tonnoz.adventofcode23.eleven

import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.toCharMatrix
import kotlin.system.measureTimeMillis

const val FIRST_CHAR = 47 // starting from 47 because 46 it's '.' which was causing problems

object Day11 {
  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input11.txt".readInput().toCharMatrix()
    part1(input)
  }

  fun part1(input: ArrayList<CharArray>) {
    val time = measureTimeMillis {
      val (universe, galaxyPairs) = createUniverseAndGalaxyPairs(input)
      val galaxyPairsWithDistance = updateGalaxyPairsWithDistances(galaxyPairs, universe)
      println("Part1: ${galaxyPairsWithDistance.sumOf { it.distance }}")
    }
    println("P1 time: $time ms")
  }

  data class Galaxy(val row: Int, val column: Int, val value: Char)
  data class GalaxyPair(val a: Galaxy, val b: Galaxy, val distance: Int = 0)

  private fun updateGalaxyPairsWithDistances(galaxyPairs: Set<GalaxyPair>, universe: List<List<Galaxy>>) =
    galaxyPairs.map { addDistanceToGalaxyPair(it, universe) }.toSet()

  private fun addDistanceToGalaxyPair(it: GalaxyPair, universe: List<List<Galaxy>>) =
    it.copy(a = it.a, b = it.b, distance = it.calculateDistanceBetween(universe))

  private fun GalaxyPair.calculateDistanceBetween(universe: List<List<Galaxy>>): Int {
    val (smallICol, bigICol) = listOf(this.a.column, this.b.column).sorted()
    val (smallIRow, bigIRow) = listOf(this.a.row, this.b.row).sorted()
    val expandedSpaceRow = countNrExpandedSpace(smallIRow, bigIRow, universe, ::isExpandedSpacesRow)
    val expandedSpaceColumn = countNrExpandedSpace(smallICol, bigICol, universe, ::isExpandedSpacesColumn)
    val rowDistance = bigIRow - smallIRow + expandedSpaceRow
    val colDistance = bigICol - smallICol + expandedSpaceColumn
    return colDistance + rowDistance
  }

  private fun countNrExpandedSpace(
    smallI: Int,
    bigI: Int,
    universe: List<List<Galaxy>>,
    isExpandedFunction: (List<List<Galaxy>>, Int) -> Int
  )= (smallI..<bigI).sumOf { isExpandedFunction(universe, it) }


  private fun Boolean.toInt() = if(this) 1 else 0
  private fun isExpandedSpacesRow(universe:List<List<Galaxy>>, row: Int) = universe[row].all { it.value == '.' }.toInt()
  private fun isExpandedSpacesColumn(universe: List<List<Galaxy>>, col: Int) = universe.all { it[col].value == '.' }.toInt()


  private fun List<List<Galaxy>>.findGalaxy(aChar: Char) : Galaxy {
    this.forEach { it.forEach { galaxy -> if(galaxy.value == aChar) return galaxy } }
    throw IllegalArgumentException("Not possible to find galaxy with char $aChar")
  }

  private fun createUniverseAndGalaxyPairs(input: ArrayList<CharArray>): Pair<List<List<Galaxy>>, Set<GalaxyPair>> {
    var counter = FIRST_CHAR
    val universe = input.mapIndexed { rowI, row ->
      row.mapIndexed { columnI, aChar ->
        if (aChar == '#') Galaxy(rowI, columnI, counter++.toChar()) else Galaxy(rowI, columnI, aChar)
      }
    }
    return Pair(universe, createGalaxyPairs(counter, universe))
  }

  private fun createGalaxyPairs(counter: Int, universe: List<List<Galaxy>>): Set<GalaxyPair> {
    val galaxyPairs = mutableSetOf<GalaxyPair>()
    for (i in FIRST_CHAR..<counter) {
      for (j in i..<counter) {
        if (i != j) galaxyPairs.add(GalaxyPair(universe.findGalaxy(i.toChar()), universe.findGalaxy(j.toChar())))
      }
    }
    return galaxyPairs.toSet()
  }

}