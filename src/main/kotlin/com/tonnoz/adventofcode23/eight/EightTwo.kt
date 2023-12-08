package com.tonnoz.adventofcode23.eight

import com.tonnoz.adventofcode23.utils.readInput


object EightTwo {

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputEight.txt".readInput()
    problemTwo(input)
  }

  private fun problemTwo(input: List<String>) {
    val time = System.currentTimeMillis()
    val instructions = input[0]
    val nodes = input.drop(2).map {
      val (name, left, right) = nodesRegex.find(it)!!.destructured
      Node(name, left, right)
    }.associateBy{it.name}
    nodes
      .keys
      .filter { it.endsWith(START_LETTER) }
      .map { initialNode ->
        var stepCount = 0
        var nodeName = initialNode
        val visitedPositions = HashMap<InstructionPosition, Int>()
        val stepsWhenEndingWithZ = ArrayList<Int>()
        while (true) {
          val instructionIndex = stepCount % instructions.length
          val currentPosition = InstructionPosition(instructionIndex, nodeName)
          if (visitedPositions.containsKey(currentPosition)) break
          visitedPositions[currentPosition] = stepCount
          val currentInstruction = instructions[instructionIndex]
          val node = nodes[nodeName]!!
          nodeName = when (currentInstruction){ LEFT -> node.left else -> node.right }
          stepCount++
          if (nodeName.endsWith(END_LETTER)) stepsWhenEndingWithZ.add(stepCount)
        }
        val loopStartStep = visitedPositions[InstructionPosition(stepCount % instructions.length, nodeName)]!!
        val endAtZStep = stepsWhenEndingWithZ.single() //should always have one element!
        LoopInfo(loopStartStep, stepCount - loopStartStep, endAtZStep)
      }
      .fold(1L){ acc, curr -> leastCommonMultiple(acc, curr.loopLength.toLong()) }
      .let { println(it) }
    println("Time p1: ${System.currentTimeMillis() - time} ms")
  }

  private const val START_LETTER = 'A'
  private const val END_LETTER = 'Z'
  private const val LEFT = 'L'
  private data class Node(val name: String, var left: String, var right: String)
  private tailrec fun greatestCommonDivisor(a: Long, b: Long): Long = if (b == 0L) a else greatestCommonDivisor(b, a % b)
  private fun leastCommonMultiple(a: Long, b: Long) = a * b / greatestCommonDivisor(a, b)
  private data class InstructionPosition(val index: Int, val nodeName: String)
  private data class LoopInfo(val firstOccurrence: Int, val loopLength: Int, val endAtZIndex: Int)
  private val nodesRegex = """(\w{3})\s*=\s*\((\w{3}),\s*(\w{3})\)""".toRegex()

}