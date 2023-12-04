package com.tonnoz.adventofcode23.four

import com.tonnoz.adventofcode23.one.readFileAsAList
import com.tonnoz.adventofcode23.two.COLON
import com.tonnoz.adventofcode23.two.SPACE
import kotlin.math.pow

const val PIPE = "|"

object Four{

  private fun Set<Int>.countPoints(): Int =
    this.toList().foldIndexed(0) { index, _, _ -> 2.toDouble().pow(index).toInt() }

  private fun String.spacedStringsToCardValues(): HashSet<Int> = this.split(SPACE)
    .filter{ it.isNotEmpty() }
    .map{ it.trim().toInt() }
    .toHashSet()

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputFour.txt".readFileAsAList()
    input.sumOf {
      val line = it.split(COLON, limit = 2).last()
      val (winningCardsString, myCardString) = line.split(PIPE, limit = 2)
      val winningCards = winningCardsString.spacedStringsToCardValues()
      val myCards = myCardString.spacedStringsToCardValues()
      winningCards.intersect(myCards).countPoints()
    }.let { println(it) }

  }

}


