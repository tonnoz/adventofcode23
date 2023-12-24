package com.tonnoz.adventofcode23.day19

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.system.measureTimeMillis

object Day19 {

  data class Workflow(val name: String, val rules: List<Rule>)
  data class Rule(val part: Char, val next: String, val condition: ((Char, Int) -> Boolean)? = null) {
    fun check(aPart: Char, aRating: Int) = condition?.invoke(aPart, aRating) ?: true
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
        if (processLastRule(rule, partMap, partListIndex, acceptedParts, workflows, toProcess)) break
        val rating = partMap[rule.part] ?: continue
        if (processRule(rule, rating, partMap, partListIndex, acceptedParts, workflows, toProcess)) break
      }
    }
    return acceptedParts.sum().toLong()
  }

  private fun makePartListToWorkflow(ratings: PartsRating, workflows: List<Workflow>): MutableList<Pair<Int, Workflow>> =
    ratings.parts.indices.map { i -> Pair(i, workflows.first { it.name == "in" }) }.toMutableList()

  private fun processLastRule(rule: Rule, partMap: Map<Char, Int>, partListIndex: Int, acceptedParts: MutableList<Int>, workflows: List<Workflow>, toProcess: MutableList<Pair<Int, Workflow>>) =
    when {
      rule.part == '*' && rule.next == "R" -> true
      rule.part == '*' && rule.next == "A" -> { acceptedParts.addAll(partMap.values); true }
      rule.part == '*' -> { toProcess.add(0, Pair(partListIndex, workflows.first { it.name == rule.next })); true }
      else -> false
    }

  private fun processRule(rule: Rule, rating: Int, partMap: Map<Char, Int>, partListIndex: Int, acceptedParts: MutableList<Int>, workflows: List<Workflow>, toProcess: MutableList<Pair<Int, Workflow>>) =
    when {
      rule.check(rule.part, rating) && rule.next == "A" -> { acceptedParts.addAll(partMap.values); true }
      rule.check(rule.part, rating) && rule.next == "R" -> true
      rule.check(rule.part, rating) -> { toProcess.add(0, Pair(partListIndex, workflows.first { it.name == rule.next })); true }
      else -> false
    }

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
    val rules = rulesS.split(",").dropLast(1).map { rule ->
      val (part, operator) = rule.first() to rule[1]
      val (value, nextWorkflow) = rule.substring(2).split(":")
      when (operator) {
        '>' -> Rule(part, nextWorkflow) { aPart, aRating -> part == aPart && aRating > value.toInt() }
        '<' -> Rule(part, nextWorkflow) { aPart, aRating -> part == aPart && aRating < value.toInt() }
        else -> throw IllegalArgumentException("Unknown operator")
      }
    }
    val fallbackRule = Rule('*', rulesS.split(",").takeLast(1).first())
    Workflow(name, rules + fallbackRule)
  }
}