package com.tonnoz.adventofcode23.day19

import com.tonnoz.adventofcode23.utils.println
import com.tonnoz.adventofcode23.utils.readInput
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

object Day19Part2 {

  data class Workflow(val name: String, val rules: List<Rule>)
  data class Rule(val part: Char, val next: String, val operator: Char, val value: Int){ fun greater() = operator == '>' }
  data class Ranges(var x: IntRange, var m: IntRange, var a: IntRange, var s: IntRange, val wfName: String) {
    operator fun get(property: Char): IntRange {
      return when (property) {
        'x' -> x
        'm' -> m
        'a' -> a
        's' -> s
        else -> throw IllegalArgumentException("Unknown part")
      }
    }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = "input19.txt".readInput()
    println("time part1: ${measureTimeMillis { part2(input).println() }}ms")
  }

  private fun part2(input: List<String>): Long {
    val (workflowsS, _) = input.splitByEmptyLine()
    val workflows = parseWorkflows(workflowsS)
    val initialRanges = Ranges(1..4000, 1..4000, 1..4000, 1..4000, "in")
    return processWorkflows(workflows, initialRanges)
  }

  private fun processWorkflows(workflows: List<Workflow>, initialRanges: Ranges): Long {
    val toProcess = mutableListOf(initialRanges)
    val acceptedRanges = mutableListOf<Ranges>()
    while (toProcess.isNotEmpty()) {
      val next = toProcess.removeFirst().let { range ->
        workflows.first { it.name == range.wfName }.rules.mapNotNull { rule ->
          val pr = range[rule.part]
          val rightR = rightRange(rule, pr)
          val leftR = leftRange(rule, pr)
          when (rule.part) {
            'x' -> { range.x = leftR; Ranges(rightR, range.m, range.a, range.s, rule.next) }
            'm' -> { range.m = leftR; Ranges(range.x, rightR, range.a, range.s, rule.next) }
            'a' -> { range.a = leftR; Ranges(range.x, range.m, rightR, range.s, rule.next) }
            's' -> { range.s = leftR; Ranges(range.x, range.m, range.a, rightR, rule.next) }
            else -> null
          }
        }
      }
      acceptedRanges += next.filter { it.wfName  == "A" }
      toProcess += next.filterNot { it.isInvalid() }
    }
    return acceptedRanges.sumOf {
      (it.x.last.toLong() - it.x.first + 1) *
      (it.m.last - it.m.first + 1) *
      (it.a.last - it.a.first + 1) *
      (it.s.last - it.s.first + 1)
    }
  }

  private fun Ranges.isInvalid() =
    listOf(this.x, this.m, this.a, this.s).any { it.isInvalid() } || this.isFinalState()

  private fun Ranges.isFinalState() = this.wfName == "A" || this.wfName == "R"
  private fun IntRange.isInvalid() = this.first > this.last

  private fun leftRange(rule: Rule, pr: IntRange) =
    if (rule.greater()) pr.first..min(rule.value, pr.last) else max(rule.value, pr.first)..pr.last

  private fun rightRange(rule: Rule, pr: IntRange) =
    if (rule.greater()) max(rule.value + 1, pr.first)..pr.last else pr.first..min(rule.value - 1, pr.last)

  private fun List<String>.splitByEmptyLine() =
    this.takeWhile { it.isNotEmpty() } to this.dropWhile { it.isNotEmpty() }.drop(1)

  private fun parseWorkflows(workflowsS: List<String>) = workflowsS.map { workflow ->
    val (name, rulesS) = workflow.dropLast(1).split("{")
    val rulesSplit = rulesS.split(",")
    val rules = rulesSplit.dropLast(1).map { rule ->
      val (part, operator) = rule.first() to rule[1]
      val (value, nextWorkflow) = rule.substring(2).split(":")
      Rule(part, nextWorkflow, operator, value.toInt())
    }
    val fallbackRule = Rule('x', rulesSplit.last(), '>', -1)
    Workflow(name, rules + fallbackRule)
  }
}