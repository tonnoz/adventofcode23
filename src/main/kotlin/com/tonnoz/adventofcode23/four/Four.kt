package com.tonnoz.adventofcode23.four

import com.tonnoz.adventofcode23.two.COLON
import com.tonnoz.adventofcode23.two.SPACE
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.math.pow

const val PIPE = "|"

object Four{

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputFour.txt".readInput()
    problemOne(input)
    problemTwo(input)
  }

  //PROBLEM 1
  private fun Set<Int>.countPoints(): Int = 2.toDouble().pow(this.size-1).toInt()

  private fun String.spacedStringsToCardValues(): HashSet<Int> = this.split(SPACE)
    .filter{ it.isNotEmpty() }
    .map{ it.trim().toInt() }
    .toHashSet()

  private fun problemOne(input:List<String>){
    input.sumOf {
      val line = it.split(COLON, limit = 2).last()
      val (winningCardsString, myCardString) = line.split(PIPE, limit = 2)
      val winningCards = winningCardsString.spacedStringsToCardValues()
      val myCards = myCardString.spacedStringsToCardValues()
      winningCards.intersect(myCards).countPoints()
    }.let { println("solution to problem one is $it") }
  }



  //PROBLEM 2
  data class Game(val winningCards: HashSet<Int>, val myCards: HashSet<Int>)

  private fun problemTwo(input:List<String>) {
    input
      .parseGame()
      .toTotalScratchCards()
      .let { println("solution to problem two is $it") }
  }
  private fun List<String>.parseGame(): List<Game> = this.map{
    val line = it.split(COLON, limit = 2).last()
    val (winningCardsString, myCardString) = line.split(PIPE, limit = 2)
    val winningCards = winningCardsString.spacedStringsToCardValues()
    val myCards = myCardString.spacedStringsToCardValues()
    Game(winningCards, myCards)
  }

  private fun List<Game>.toTotalScratchCards(): Int {
    val copies = List(this.size){ 1 }.toMutableList()
    this.forEachIndexed { i, game ->
      val (winningCards, myCards) = game
      val matchingNumbers = myCards.intersect(winningCards).count()
      (1..matchingNumbers).forEach{ copies[i + it] += copies[i] } //here we are adding the number of copies of the game to the next games
    }
    return copies.sum()
  }
}




