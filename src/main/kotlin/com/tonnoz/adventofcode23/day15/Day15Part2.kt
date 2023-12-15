package com.tonnoz.adventofcode23.day15

import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

typealias BoxNr = Int

object Day15Part2 {

  data class Box(val lensesBox: MutableList<Lens>){
    data class Lens(val label: String, val value: String)
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input15.txt".readInput().first().split(",")
    val time = measureTimeMillis {
      val boxes: MutableMap<BoxNr, Box> = mutableMapOf()
      input.forEach { key ->
        val label = getLabel(key)
        val (hashedLabel, operator, lens) = getOtherVariables(label, key)
        when (operator) {
          "-" -> boxes.minusOp(hashedLabel, label)
          "=" -> boxes.equalOp(hashedLabel, label, lens)
        }
      }
      println("Part2: ${boxes.sumLenses()}")
    }
    println("P2 time: $time ms")
  }


  private fun MutableMap<BoxNr, Box>.sumLenses() = this.entries.fold(0L){ acc, curr -> acc + curr }

  private fun getOtherVariables(label: String, key: String): Triple<Int, String, String> {
    val hashedLabel = hash(label)
    val operator = key.first { it == '-' || it == '=' }.toString()
    val lens = key.substring(key.indexOfFirst { it == '-' || it == '=' } + 1)
    return Triple(hashedLabel, operator, lens)
  }

  private fun MutableMap<BoxNr, Box>.equalOp(hashedLabel: Int, label: String, lens: String) {
    val box = this.getOrPut(hashedLabel) { Box(mutableListOf(Box.Lens(label, lens))) }
    box.replaceOrAddLensWithLabel(label, lens)
  }

  private fun MutableMap<BoxNr, Box>.minusOp(hashedLabel: Int, label: String) {
    this[hashedLabel]?.removeFirstLensWithLabel(label)
    if (this[hashedLabel] != null && this[hashedLabel]!!.lensesBox.size == 0) this.remove(hashedLabel)
  }

  infix operator fun Long.plus(other: MutableMap.MutableEntry<BoxNr, Box>): Long =
    other.value.lensesBox.foldIndexed(0L) { i, acc, lens ->
      acc + lens.value.toLong() * (i + 1) * (other.key + 1)
    } + this

  private fun hash(key: String) =
    getLabel(key).fold(0) { acc, curr -> ((acc + curr.code) * 17) % 256 }

  private fun getLabel(key: String) = key.takeWhile { it != '-' && it != '=' }

  private fun Box.removeFirstLensWithLabel(label: String) =
    this.lensesBox.remove(this.lensesBox.firstOrNull { it.label == label })

  private fun Box.replaceOrAddLensWithLabel(lensLabel: String, lensValue: String) {
    this.lensesBox.find { it.label == lensLabel }?.let { found ->
      val aIndex = this.lensesBox.indexOf(found)
      this.lensesBox[aIndex] = Box.Lens(label = found.label, value = lensValue)
    } ?: this.lensesBox.add(Box.Lens(label = lensLabel, value = lensValue))
  }
}
