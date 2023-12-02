package com.tonnoz.adventofcode23.two

import com.tonnoz.adventofcode23.one.readFileAsAList
typealias Color = String

//max 12 red cubes, 13 green cubes, and 14 blue cubes for the first problem
data class GameRound(val blueCubes: Int?, val greenCubes: Int?, val redCubes:Int?)
data class Game(val gameNr: Int, val rounds: List<GameRound>, val minCubes: Map<Color, Int>)

private const val SPACE = " "
private const val SEMICOLON = ";"
private const val COLON = ":"
private const val COMMA = ","
fun GameRound.isInvalid(): Boolean = (this.redCubes ?: 0) > 12 || (this.greenCubes ?: 0) > 13 || (this.blueCubes ?: 0) > 14

fun String.toGame(): Game {
  val line = this.split(COLON)
  val gameNr = line.first().split(SPACE).last().toInt()
  val roundsString = line.last().split(SEMICOLON)
  val rounds = roundsString.map {
    it.split(COMMA)
      .map { gameThrow -> gameThrow.trim() }
      .map { gameThrowT ->
        val (first, second) = gameThrowT.split(SPACE)
        first to second
      }.associate { (first, second) ->
        second to first.toInt()
      }
  }.map { GameRound(it["blue"], it["green"], it["red"]) }
  return Game(gameNr, rounds, emptyMap())
}

fun Game.toGameExtra(): Game {
  val maxBlue = this.rounds.maxBy { it.blueCubes ?: 0 }.blueCubes ?: 1
  val maxRed = this.rounds.maxBy { it.redCubes ?: 0 }.redCubes ?: 1
  val maxGreen = this.rounds.maxBy { it.greenCubes ?: 0 }.greenCubes ?: 1
  return Game(this.gameNr, this.rounds, mapOf("blue" to maxBlue, "red" to maxRed, "green" to maxGreen))
}

fun main() {
  val input = "inputTwo.txt".readFileAsAList()
  val games = input.map { it.toGame() }

  games
    .filter { it.rounds.none { gameR -> gameR.isInvalid() } }
    .fold(0) { acc, curr -> acc + curr.gameNr}
    .let { println(it) } //answer first problem is 2169

  games
    .map { it.toGameExtra() }
    .fold(0) { acc, curr -> acc + curr.minCubes.values.reduce(Int::times) }
    .let { println(it) } //answer second problem is 60948

}