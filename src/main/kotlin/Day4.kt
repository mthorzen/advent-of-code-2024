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
                if (x > grid.lastIndex || y > grid.lastIndex || x < 0 || y < 0) return false
                if (x !in grid[y].indices) return false
                if (y !in yRows) return false
                if (grid[y][x] != word[i]) return false
            }
            return true
        }

        fun countDirection(xStep: Int, yStep: Int): Int {
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

    // Function to find occurrences of MAS in a diagonal cross with the A as the center
    fun findDiagonalCrosses(content: String, word: String): Int {
        if (word != "MAS" || word.length != 3) return 0

        val grid = content.lines().map { it.toCharArray() }
        val yRows = grid.indices
        var count = 0

        for (y in 1 until grid.lastIndex - 1) {
            for (x in 1 until grid[y].lastIndex) {
                if (grid[y][x] == 'A') {
                    println("Found A at ($x, $y)")
                    if (((grid[y - 1][x - 1] == 'M' && grid[y + 1][x + 1] == 'S') ||
                                (grid[y - 1][x - 1] == 'S' && grid[y + 1][x + 1] == 'M')) &&
                        ((grid[y + 1][x - 1] == 'M' && grid[y - 1][x + 1] == 'S') ||
                                (grid[y + 1][x - 1] == 'S' && grid[y - 1][x + 1] == 'M'))) {
                        println("Found cross at ($x, $y)")
                        count++
                    }
                }

            }
        }

        return count
    }

    val counts = findOccurrencesInAllDirections(fileContent, "XMAS")

    val diagonalCrossesCount = findDiagonalCrosses(fileContent, "MAS")

    // Counts of XMAS occurrences
    println("Counts: $counts")

    // Counts of diagonal MAS crosses where A is the center
    println("Diagonal MAS Crosses: $diagonalCrossesCount")
}