package com.tonnoz.adventofcode23.eleven

import com.tonnoz.adventofcode23.utils.readInput
import com.tonnoz.adventofcode23.utils.toCharMatrix
import kotlin.system.measureTimeMillis

const val EXPANDING_FACTOR = 1_000_000L

object Day11Part2 {
  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input11.txt".readInput().toCharMatrix()
    val time = measureTimeMillis {
      val (universe, galaxyPairs) = createUniverseAndGalaxyPairs(input)
      val distances = galaxyPairs.map { it.calculateDistanceBetween(universe) }
      println("Part2: ${distances.sum()}")
    }
    println("P2 time: $time ms")
  }

  data class Galaxy(val row: Int, val column: Int, val value: Char)
  data class GalaxyPair(val a: Galaxy, val b: Galaxy)

  private fun GalaxyPair.calculateDistanceBetween(universe: List<List<Galaxy>>): Long {
    val (smallICol, bigICol) = listOf(this.a.column, this.b.column).sorted()
    val (smallIRow, bigIRow) = listOf(this.a.row, this.b.row).sorted()
    val expandedSpaceRow = countNrExpandedSpace(smallIRow, bigIRow, universe, ::isExpandedSpacesRow)
    val expandedSpaceColumn = countNrExpandedSpace(smallICol, bigICol, universe, ::isExpandedSpacesColumn)
    val rowDistance = bigIRow.toLong() - smallIRow.toLong() + expandedSpaceRow
    val colDistance = bigICol.toLong() - smallICol.toLong() + expandedSpaceColumn
    return colDistance + rowDistance
  }

  private fun countNrExpandedSpace(smallI: Int, bigI: Int, universe: List<List<Galaxy>>, isExpandedFunction: (List<List<Galaxy>>, Int) -> Long) =
    (smallI..<bigI).sumOf { isExpandedFunction(universe, it) }

  private fun Boolean.toLong() = if(this) EXPANDING_FACTOR-1 else 0L
  private fun isExpandedSpacesRow(universe:List<List<Galaxy>>, row: Int) = universe[row].all { it.value == '.' }.toLong()
  private fun isExpandedSpacesColumn(universe: List<List<Galaxy>>, col: Int) = universe.all { it[col].value == '.' }.toLong()

  private fun List<List<Galaxy>>.findGalaxy(aChar: Char) : Galaxy { // naive linear search
    this.forEach { it.forEach { galaxy -> if(galaxy.value == aChar) return galaxy } }
    throw IllegalArgumentException("Not possible to find galaxy with char $aChar")
  }

  private fun createUniverseAndGalaxyPairs(input: ArrayList<CharArray>): Pair<List<List<Galaxy>>, Set<GalaxyPair>> {
    var counter = FIRST_CHAR
    val universe = input.mapIndexed { rowI, row -> row.mapIndexed { columnI, aChar ->
      if (aChar == '#') Galaxy(rowI, columnI, counter++.toChar()) else Galaxy(rowI, columnI, aChar)
    }}
    return Pair(universe, createGalaxyPairs(counter, universe))
  }

  private fun createGalaxyPairs(counter: Int, universe: List<List<Galaxy>>): Set<GalaxyPair> {
    val galaxyPairs = mutableSetOf<GalaxyPair>()
    for (i in FIRST_CHAR..<counter) { for (j in i..<counter) {
      if (i != j) galaxyPairs.add(GalaxyPair(universe.findGalaxy(i.toChar()), universe.findGalaxy(j.toChar())))
    }}
    return galaxyPairs.toSet()
  }

}