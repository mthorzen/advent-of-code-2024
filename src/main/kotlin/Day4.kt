package com.nordoba

import java.io.File


fun main() {
    println("Day 4!")

    val fileContent = File("src/main/resources/day4-input.txt").readText()
    println(fileContent)



    fun findOccurrencesInAllDirections(content: String, word: String): Int {
        val grid = content.lines().map { it.toCharArray() }
        val wordLength = word.length

        val yRows = grid.indices
        fun checkWord(xStart: Int, yStart: Int, xStep: Int, yStep: Int): Boolean {
            for (i in 0 until wordLength) {
                val x = xStart + i * xStep
                val y = yStart + i * yStep
                println("($x, $y)")
                if (x > grid.lastIndex || y > grid.lastIndex || x < 0 || y < 0) return false
                if (x !in grid[y].indices) return false
                if (y !in yRows) return false
                if (grid[y][x] != word[i]) return false
            }
            return true
        }

        fun countDirection(xStep: Int, yStep: Int): Int {
            println("Checking direction: $xStep, $yStep")
            var count = 0
            for (y in yRows) {
                val xRows = grid[y].indices
                for (x in xRows) {
                    if (checkWord(x, y, xStep, yStep)) count++
                    if (checkWord(x, y, -xStep, -yStep)) count++  // Support for backwards direction
                }
            }
            return count
        }



        return countDirection(1, 0) + // Horizontal right and left
                countDirection(0, 1) + // Vertical down and up
                countDirection(1, 1) + // Diagonal down-right and up-left
                countDirection(1, -1)  // Diagonal up-right and down-left
    }

    val counts = findOccurrencesInAllDirections(fileContent, "XMAS")

    // Part 1: first try: 2639 (correct)
    println("Counts: $counts")
}