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

    // Parse input
    val (orderingRules, updates) = parseInput(fileContent)

    // Process each update and sum the middle values of the correctly ordered ones
    val sumOfMiddlePages = updates
        .filter { update -> isCorrectOrder(orderingRules, update) }
        .sumOf { it[it.size / 2] }

    // first try: 4609 (correct)
    println("Sum of middle page numbers: $sumOfMiddlePages")

}