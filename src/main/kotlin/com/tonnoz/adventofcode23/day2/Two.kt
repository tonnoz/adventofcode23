package com.tonnoz.adventofcode23.day2

import com.tonnoz.adventofcode23.utils.readInput

typealias Color = String

const val SPACE = " "
const val SEMICOLON = ";"
const val COLON = ":"
const val COMMA = ","
object Two {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputTwo.txt".readInput()
    val games = input.map { it.toGame() }

    games
      .filter { it.rounds.none { gameR -> gameR.isInvalid() } }
      .sumOf { it.gameNr }
      .let { println(it) } //answer first problem

    games
      .map { it.toGameExtra() }
      .sumOf { it.minCubes.values.reduce(Int::times) }
      .let { println(it) } //answer second problem
  }

  //PROBLEM ONE

  //max 12 red cubes, 13 green cubes, and 14 blue cubes
  data class GameRound(val blueCubes: Int?, val greenCubes: Int?, val redCubes: Int?)
  data class Game(val gameNr: Int, val rounds: List<GameRound>, val minCubes: Map<Color, Int>)
  private fun GameRound.isInvalid(): Boolean =
    (this.redCubes ?: 0) > 12 || (this.greenCubes ?: 0) > 13 || (this.blueCubes ?: 0) > 14

  private fun String.toGame(): Game {
    val line = this.split(COLON)
    val gameNr = line.first().split(SPACE).last().toInt()
    val roundsString = line.last().split(SEMICOLON)
    val rounds = roundsString.map {
      it.split(COMMA)
        .map { gameThrow -> gameThrow.trim() }
        .map { gameThrowT -> val (first, second) = gameThrowT.split(SPACE); first to second }
        .associate { (first, second) -> second to first.toInt() }
    }.map { GameRound(it["blue"], it["green"], it["red"]) }
    return Game(gameNr, rounds, emptyMap())
  }


  //PROBLEM 2:
  private fun Game.toGameExtra(): Game {
    val maxBlue = this.rounds.maxBy { it.blueCubes ?: 0 }.blueCubes ?: 1
    val maxRed = this.rounds.maxBy { it.redCubes ?: 0 }.redCubes ?: 1
    val maxGreen = this.rounds.maxBy { it.greenCubes ?: 0 }.greenCubes ?: 1
    return Game(this.gameNr, this.rounds, mapOf("blue" to maxBlue, "red" to maxRed, "green" to maxGreen))
  }
}