package com.tonnoz.adventofcode23.day19

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

object Day19 {

  data class Workflow(val name: String, val rules: List<Rule>)
  data class Rule(val part: Char, val next: String, val condition: ((Char, Int) -> Boolean)? = null) {
    fun check(aPart: Char, aRating: Int?) = if(aRating == null) false else condition?.invoke(aPart, aRating) ?: true
  }
  data class PartsRating(val parts: List<Map<Char, Int>>)

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input19.txt".readInput()
    println("time part1: ${measureTimeMillis { part1(input).println() }}ms")
  }

  private fun part1(input: List<String>): Long {
    val (workflowsS, ratingsS) = input.splitByEmptyLine()
    val ratings = parseRatings(ratingsS)
    val workflows = parseWorkFlows(workflowsS)
    return processWorkflows(workflows, ratings)
  }

  private fun processWorkflows(workflows: List<Workflow>, ratings: PartsRating): Long {
    val toProcess = makePartListToWorkflow(ratings, workflows)
    val acceptedParts = mutableListOf<Int>()
    while (toProcess.isNotEmpty()) {
      val (partListIndex, workflow) = toProcess.removeFirst()
      val partMap = ratings.parts[partListIndex]
      for (rule in workflow.rules) {
        when {
          rule.next == "R" && rule.part == '*' -> { break }
          rule.next == "A" && rule.part == '*' -> { acceptedParts.addAll(partMap.values); break }
                              rule.part == '*' -> { toProcess.add(Pair(partListIndex, workflows.first { it.name == rule.next })); break }
          rule.next == "R" && rule.check(rule.part, partMap[rule.part]) -> { break }
          rule.next == "A" && rule.check(rule.part, partMap[rule.part]) -> { acceptedParts.addAll(partMap.values); break }
                              rule.check(rule.part, partMap[rule.part]) -> { toProcess.add(Pair(partListIndex, workflows.first { it.name == rule.next })); break }
        }
      }
    }
    return acceptedParts.sum().toLong()
  }

  private fun makePartListToWorkflow(ratings: PartsRating, workflows: List<Workflow>): MutableList<Pair<Int, Workflow>> =
    ratings.parts.indices.map { i -> Pair(i, workflows.first { it.name == "in" }) }.toMutableList()


  private fun List<String>.splitByEmptyLine() =
    this.takeWhile { it.isNotEmpty() } to this.dropWhile { it.isNotEmpty() }.drop(1)

  private fun parseRatings(ratingsS: List<String>) =
    PartsRating(
      ratingsS.map {
        it.drop(1).dropLast(1).split(",").associate { rating ->
          val (key, value) = rating.split("=")
          key.first() to value.toInt()
        }
      }
    )

  private fun parseWorkFlows(workflowsS: List<String>) = workflowsS.map { workflow ->
    val (name, rulesS) = workflow.dropLast(1).split("{")
    val rulesSplit = rulesS.split(",")
    val rules = rulesSplit.dropLast(1).map { rule ->
      val (part, operator) = rule.first() to rule[1]
      val (value, nextWorkflow) = rule.substring(2).split(":")
      when (operator) {
        '>' -> Rule(part, nextWorkflow) { aPart, aRating -> part == aPart && aRating > value.toInt() }
        '<' -> Rule(part, nextWorkflow) { aPart, aRating -> part == aPart && aRating < value.toInt() }
        else -> throw IllegalArgumentException("Unknown operator")
      }
    }
    val fallbackRule = Rule('*', rulesSplit.last())
    Workflow(name, rules + fallbackRule)
  }
}