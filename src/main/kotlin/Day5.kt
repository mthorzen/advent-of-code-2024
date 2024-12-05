package com.nordoba

import java.io.File


fun main() {
    println("Day 5!")

    val fileContent = File("src/main/resources/day5-input.txt").readText()
    println(fileContent)

    // Parses the input to separate order rules and updates
    fun parseInput(input: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val sections = input.split("\n\n")

        // Parse ordering rules
        val orderingRules = sections[0].lines().map {
            val (x, y) = it.split("|").map(String::toInt)
            Pair(x, y)
        }

        // Parse updates
        val updates = sections[1].lines().filter { line -> line.isNotEmpty() }.map { line ->
            println("line: " + line)
            line.trim().split(",").map(String::toInt)
        }

        return Pair(orderingRules, updates)
    }

    // Checks if an update is in the correct order
    fun isCorrectOrder(orderingRules: List<Pair<Int, Int>>, update: List<Int>): Boolean {
        for ((x, y) in orderingRules) {
            if (update.contains(x) && update.contains(y)) {
                if (update.indexOf(x) > update.indexOf(y)) {
                    return false
                }
            }
        }
        return true
    }

    fun sortCorrectly(orderingRules: List<Pair<Int, Int>>, update: List<Int>) : List<Int> {
        val sortedUpdate = update.toMutableList()
        do {
            var madeChange = false
            for ((x, y) in orderingRules) {
                val indexX = sortedUpdate.indexOf(x)
                val indexY = sortedUpdate.indexOf(y)
                if (indexX != -1 && indexY != -1 && indexX > indexY) {
                    sortedUpdate.removeAt(indexX)
                    sortedUpdate.add(indexY, x)
                    madeChange = true
                }
            }
        } while (madeChange)
        return sortedUpdate
    }

    // Parse input
    val (orderingRules, updates) = parseInput(fileContent)

    // Process each update and sum the middle values of the correctly ordered ones
    val sumOfMiddlePages = updates
        .filter { update -> isCorrectOrder(orderingRules, update) }
        .sumOf { it[it.size / 2] }

    // Process each update and sum the middle values of the incorrectly ordered ones
    val sumOfMiddlePagesIncorrectlyOrdered = updates
        .filter { update -> !isCorrectOrder(orderingRules, update) }
        .map { update -> sortCorrectly(orderingRules, update) }
        .sumOf { it[it.size / 2] }

    // Part 1: first try: 4609 (correct)
    println("Part 1 - Sum of middle page numbers: $sumOfMiddlePages")

    // Part 2: first try: 5723 (correct)
    println("Part 2 - Sum of middle page numbers: $sumOfMiddlePagesIncorrectlyOrdered")

}


