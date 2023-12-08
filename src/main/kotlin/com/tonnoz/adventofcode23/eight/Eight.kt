package com.tonnoz.adventofcode23.eight

import com.tonnoz.adventofcode23.utils.readInput


object Eight {

  val nodesRegex = """(\w{3})\s*=\s*\((\w{3}),\s*(\w{3})\)""".toRegex()

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "inputEight.txt".readInput()
    problemOne(input)
//    problemTwo(input)
  }

  private data class Node(val name: String, val left: String, val right: String)

  private fun problemOne(input: List<String>){
    val time = System.currentTimeMillis()
    val instructions = input[0].split("").dropLast(1).drop(1).map { it.first() }
    val nodes = input.drop(2).map {
      val matchResult = nodesRegex.find(it);
      val (root, left, right) = matchResult?.destructured ?: throw Exception("Invalid input")
      Node(root, left, right)
    }.sortedBy { it.name }

    var n = nodes[0]
    var step = ""
    var iterator = instructions.iterator()
    var stepCounter: Long = -1
    do{
      stepCounter++
      step = n.name
      iterator = if(!iterator.hasNext() ) instructions.iterator() else iterator
      val a = iterator.next()
//      println("found $a")
      n = when(a){
        'L' -> nodes.find { it.name == n.left }!!
        'R' -> nodes.find { it.name == n.right }!!
        else -> error("Invalid instruction")
      }
//      println("found ${n}")

    }while (step != "ZZZ")
    println(stepCounter)

    println("Time p1: ${System.currentTimeMillis() - time} ms")
  }

}