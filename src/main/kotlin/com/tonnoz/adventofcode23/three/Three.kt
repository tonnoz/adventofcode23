package com.tonnoz.adventofcode23.three

import com.tonnoz.adventofcode23.one.readFileAsAList

val symbols = hashSetOf('@','#', '$', '%', '&', '*', '+', '-', '=', '/')


fun main() {
  val input = "inputThree.txt".readFileAsAList()
  problemOne(input)
}

private fun problemOne(input: List<String>) {
  var iLine = 0
  val resultList = mutableListOf<Int>()
  while (iLine < input.size) {
    val line = input[iLine]
    var iChar = 0
    while (iChar < line.length) {
      val aChar = line[iChar]
      if (!aChar.isDigit()) {
        iChar++
        continue
      }
      if (aChar.isACandidate(input, iLine, iChar)) {
        val aNumber = getNextNumber(line, iChar)
        iChar += aNumber.length
        resultList.add(aNumber.toInt())
        continue
      }
      iChar++
    }
    iLine++
  }
  println(resultList.sum())
}

private fun Char.isASymbol(): Boolean = symbols.contains(this)


private fun Char.isACandidate(input: List<String>, iLine: Int, iChar: Int): Boolean {
  val aNumber = getNextNumber(input[iLine], iChar)
  return checkNumberAdjacentH(aNumber, input[iLine], iChar) || checkNumberAdjacentVorD(aNumber, input, iLine, iChar, input[iLine])
}

fun checkNumberAdjacentVorD(aNumber: String, input: List<String>, iLine: Int, iChar: Int, line: String): Boolean {
  val skipTop = iLine == 0
  val skipBottom = iLine == input.size - 1
  var i = if(iChar>0) iChar-1 else iChar
  val to = if(iChar + aNumber.length <= line.length -1) iChar + aNumber.length else iChar + aNumber.length -1 // nb. there is always at least "aNumber.length" chars left in the line
  while (i <= to){
    if(!skipTop && input[iLine-1][i].isASymbol()) return true
    if(!skipBottom && input[iLine+1][i].isASymbol()) return true
    i++
  }
  return false
}

fun checkNumberAdjacentH(aNumber: String, line: String, iChar: Int): Boolean =
  (iChar-1 >= 0 && line[iChar-1].isASymbol()) || (iChar + aNumber.length < line.length && line[iChar + aNumber.length].isASymbol())


fun getNextNumber(line: String, iChar: Int): String = line.substring(iChar).takeWhile { it.isDigit() }

