package com.nordoba

import java.io.File

fun main() {
    println("Day 3!")

    val fileContent = File("src/main/resources/day3-input.txt").readText()
    println(fileContent)

    val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()
    val otherRegex = """do\(\)|don't\(\)""".toRegex()
    val allMatches = (mulRegex.findAll(fileContent).toList() + otherRegex.findAll(fileContent).toList())
        .sortedBy { it.range.first }.map { it.value }

    println("Ordered occurrences: $allMatches")

    var sum = 0
    var active = true
    for (match in allMatches) {
        if (match.equals("do()")) {
            active = true
        } else if (match.equals("don't()")) {
            active = false
        } else if (active) {
            val (a, b) = match.split("(")[1].split(")")[0].split(",")
            sum += a.toInt() * b.toInt()
        }

    }

    // Part 1: first try: 189527826 (correct)
    // Part 2: first try: 63013756
    println("Sum: $sum")
}