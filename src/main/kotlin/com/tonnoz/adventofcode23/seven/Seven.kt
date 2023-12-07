package com.tonnoz.adventofcode23.seven

import com.tonnoz.adventofcode23.one.readFileAsAList


object Seven {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputSeven.txt".readFileAsAList()
    problemOne(input)
    problemTwo(input)
  }

  private fun problemOne(input: List<String>){
    val time = System.currentTimeMillis()
    input
      .map { it.toHand() }
      .sorted()
      .foldIndexed(0){ i, acc , curr-> acc + curr.bid * (i + 1) }
      .let { println("solution p1: $it") }
    println("Time p1: ${System.currentTimeMillis() - time} ms")
  }

  private fun problemTwo(input: List<String>){
    val time = System.currentTimeMillis()
    input
      .map { it.toHand2() }
      .sorted()
      .foldIndexed(0){ i, acc , curr-> acc + curr.bid * (i + 1) }
      .let { println("solution p2: $it") }
    println("Time p2: ${System.currentTimeMillis() - time} ms")
  }

  private val cardsMap = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
  private val cardsMap2 = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J') //index 12 is J(oker)

  private fun String.toHand(): Hand {
    val (cards, bid) = this.split(" ")
    val cardsInt = cards.map { cardsMap.indexOf(it) }.toIntArray()
    val cardsValues = cards.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
    val categoryStrength = when {
      cardsValues.size == 1 -> 7
      cardsValues.size == 2 && cardsValues[0].second == 4 -> 6
      cardsValues.size == 2 && cardsValues[0].second == 3 -> 5
      cardsValues.size == 3 && cardsValues[0].second == 3 -> 4
      cardsValues.size == 3 && cardsValues[0].second == 2 && cardsValues[1].second == 2 -> 3
      cardsValues.size == 4 && cardsValues[0].second == 2 -> 2
      else -> 1
    }
    return Hand(cards, categoryStrength, bid.toShort(), cardsInt)
  }

  private fun String.toHand2(): Hand {
    val (cards, bid) = this.split(" ")
    val cardsInt = cards.map { cardsMap2.indexOf(it) }
    val categoryStrength = (0..< 12).maxOf { maybeJoker ->
      val cardsIntJokerized = cardsInt.map { if (it == 12) maybeJoker else it }
      val cardsMap = cardsIntJokerized.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
      when {
        cardsMap.size == 1 -> 7
        cardsMap.size == 2 && cardsMap[0].second == 4 -> 6
        cardsMap.size == 2 && cardsMap[0].second == 3 -> 5
        cardsMap.size == 3 && cardsMap[0].second == 3 -> 4
        cardsMap.size == 3 && cardsMap[0].second == 2 && cardsMap[1].second == 2 -> 3
        cardsMap.size == 4 && cardsMap[0].second == 2 -> 2
        else -> 1
      }
    }
    return Hand(cards, categoryStrength, bid.toShort(), cardsInt.toIntArray())
  }

  //I left cards here for debug purposes, but it can be removed
  private class Hand(val cards:String, val categoryStrength: Int, val bid: Short, val intValues : IntArray): Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
      if (categoryStrength < other.categoryStrength) return -1
      if (categoryStrength > other.categoryStrength) return 1
      for (i in intValues.indices) {
        if (intValues[i] < other.intValues[i]) return 1
        if (intValues[i] > other.intValues[i]) return -1
      }
      return 0
    }
  }

}