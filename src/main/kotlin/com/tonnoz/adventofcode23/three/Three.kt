package com.tonnoz.adventofcode23.three

import com.tonnoz.adventofcode23.one.readFileAsAList

val symbols = hashSetOf('@','#', '$', '%', '&', '*', '+', '-', '=', '/')

fun main() {
  val input = "inputThree.txt".readFileAsAList()
  problemOne(input)
  problemTwo(input)
}

fun problemTwo(input: List<String>) {
  input.flatMapIndexed { iLine, line ->
    line.mapIndexedNotNull { iChar, aChar ->
      if (aChar == '*') input.getGearsProductIfBothPresentOrElseZero(iLine, iChar)
      else null
    }
  }.sum().let {
    println("the solution to problem two is: $it") //84900879
  }
}
private fun List<String>.getGearsProductIfBothPresentOrElseZero(iLine: Int, iChar: Int): Int =
  hashSetOf(
    getGearNumberLeft(iLine, iChar),
    getGearNumberRight(iLine, iChar),
    getGearNumberTopCenter(iLine, iChar),
    getGearNumberTopLeft(iLine, iChar),
    getGearNumberTopRight(iLine, iChar),
    getGearNumberBottomCenter(iLine, iChar),
    getGearNumberBottomLeft(iLine, iChar),
    getGearNumberBottomRight(iLine, iChar)
  ).let {
    it.remove(0)
    if (it.size != 2) return 0
    return it.reduce { acc, curr -> acc * curr }
  }

private fun List<String>.getGearNumberRight(iLine: Int, iChar: Int) = this[iLine].findNumber(iChar + 1).tryToIntOrZero()
private fun List<String>.getGearNumberLeft(iLine: Int, iChar: Int) = this[iLine].findNumber(iChar - 1).tryToIntOrZero()
fun String.tryToIntOrZero(): Int = try { this.toInt() } catch (e: Exception) { 0 }
fun String.findNumber(iChar: Int): String {
  if (iChar !in this.indices || !this[iChar].isDigit()) return ""
  val startIndex = this.substring(0, iChar).indexOfLast { !it.isDigit() } + 1
  val lastIndexOfADigit = this.substring(iChar).indexOfFirst { !it.isDigit() }
  val endIndex = if(lastIndexOfADigit == -1) this.length else this.substring(iChar).indexOfFirst { !it.isDigit() } + iChar
  return if (endIndex > -1) {
    this.substring(startIndex, endIndex)
  } else {
    this.substring(startIndex)
  }
}

fun List<String>.getGearNumberTopCenter(iLine: Int, iChar: Int): Int = this.getNumber(iLine, iChar, -1,0)
fun List<String>.getGearNumberTopLeft(iLine: Int, iChar: Int): Int = this.getNumber(iLine, iChar, -1,-1)
fun List<String>.getGearNumberTopRight(iLine: Int, iChar: Int): Int = this.getNumber(iLine, iChar, -1,1)
fun List<String>.getGearNumberBottomCenter(iLine: Int, iChar: Int): Int = this.getNumber(iLine, iChar, +1,0)
fun List<String>.getGearNumberBottomLeft(iLine: Int, iChar: Int): Int  = this.getNumber(iLine, iChar, +1,-1)
fun List<String>.getGearNumberBottomRight(iLine: Int, iChar: Int): Int  = this.getNumber(iLine, iChar, +1,1)
fun List<String>.getNumber(iLine:Int, iChar:Int, lineIAddendum: Int, charIAddendum: Int): Int {
  if(iLine == 0) return 0
  if(iLine == this.size - 1) return 0
  if(!this[iLine + lineIAddendum][iChar + charIAddendum].isDigit()) return 0
  return this[iLine + lineIAddendum].findNumber(iChar + charIAddendum).tryToIntOrZero()
}









//****************************** PROBLEM ONE ******************************//

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
      if (isACandidate(input, iLine, iChar)) {
        val aNumber = line.getNextNumberRightOf(iChar)
        iChar += aNumber.length
        resultList.add(aNumber.toInt())
        continue
      }
      iChar++
    }
    iLine++
  }
  println("the result of first problem is ${resultList.sum()}") // 530849
}

private fun Char.isASymbol(): Boolean = symbols.contains(this)

private fun isACandidate(input: List<String>, iLine: Int, iChar: Int): Boolean {
  val aNumber = input[iLine].getNextNumberRightOf(iChar)
  return isNumberAdjacentHorizontal(aNumber, input[iLine], iChar) || isNumberAdjacentVerticalOrDiagonal(aNumber, input, iLine, iChar, input[iLine])
}

fun isNumberAdjacentVerticalOrDiagonal(aNumber: String, input: List<String>, iLine: Int, iChar: Int, line: String): Boolean {
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

fun isNumberAdjacentHorizontal(aNumber: String, line: String, iChar: Int): Boolean =
  (iChar-1 >= 0 && line[iChar-1].isASymbol()) || (iChar + aNumber.length < line.length && line[iChar + aNumber.length].isASymbol())


fun String.getNextNumberRightOf(iChar: Int): String = this.substring(iChar).takeWhile { it.isDigit() }
