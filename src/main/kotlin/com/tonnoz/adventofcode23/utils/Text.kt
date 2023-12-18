package com.tonnoz.adventofcode23.utils

fun Any.bold(color: String = ""): String = style("$color;1")
fun Any.underline(color: String = ""): String = style("$color;4")
fun Any.style(color: String): String { return "\u001B[${color}m$this\u001B[0m" }

fun Any.colored(fgCol:String, bgCol: String, bold: Boolean = false): String {
  return if(bold) "\u001B[${fgCol};${bgCol};1m$this\u001B[0m"
  else "\u001B[${fgCol};${bgCol}m$this\u001B[0m"
}

/**
to reset just call it with "" as color parameter TODO: make constants for colors

 *     Text Colors
 *     30     BLACK
 *     31       RED
 *     32     GREEN
 *     33    YELLOW
 *     34      BLUE
 *     35   MAGENTA
 *     36      CYAN
 *     37     WHITE
 *
 *      Background
 *     40    BLACK
 *     41      RED
 *     42    GREEN
 *     43   YELLOW
 *     44     BLUE
 *     45  MAGENTA
 *     46     CYAN
 *     47    WHITE
 *
 *      High Intensity Text
 *     90     BLACK
 *     91       RED
 *     92     GREEN
 *     93    YELLOW
 *     94      BLUE
 *     95   MAGENTA
 *     96      CYAN
 *     97     WHITE

 *
 *      High Intensity backgrounds
 *     100      BLACK
 *     101        RED
 *     102     GREEN
 *     103    YELLOW
 *     104      BLUE
 *     105   MAGENTA
 *     106      CYAN
 *     107     WHITE
 */


