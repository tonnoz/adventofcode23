package com.tonnoz.adventofcode23.two

import com.tonnoz.adventofcode23.one.readFileAsAList

//max 12 red cubes, 13 green cubes, and 14 blue cubes
data class GameRound(val blueC: Int?, val greenC: Int?, val redC:Int?)
data class Game(val gameNr: Int, val rounds: List<GameRound>)

private const val SPACE = " "
private const val SEMICOLON = ";"
private const val COLON = ":"
private const val COMMA = ","
fun GameRound.isInvalid(): Boolean = (this.redC ?: 0) > 12 || (this.greenC ?: 0) > 13 || (this.blueC ?: 0) > 14

operator fun Game.plus(anotherGame: Game): Game {
  return Game(this.gameNr + anotherGame.gameNr, emptyList())
}

fun String.toGame(): Game {
  val line = this.split(COLON)
  val gameNr = line.first().split(SPACE).last().toInt()
  val roundsString = line.last().split(SEMICOLON)
  val rounds = roundsString.map {
    it.split(COMMA)
      .map { color -> color.trim() }
      .map { gameThrow ->
        val (first, second) = gameThrow.split(SPACE)
        first to second
      }.associate { (first, second) ->
        second to first.toInt()
      }
  }.map { round ->
    GameRound(round["blue"], round["green"], round["red"])
  }
  return Game(gameNr, rounds)
}

fun main() {
  "inputTwo.txt".readFileAsAList().map { lineString -> lineString.toGame() }
    .filter{ it.rounds.none {gameR ->  gameR.isInvalid() } }
    .reduce { acc, curr -> acc + curr}
    .gameNr.let { println(it) } //answer is 2169
}