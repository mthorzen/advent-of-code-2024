package com.nordoba

import java.io.File

fun main() {
    println("Day 2!")

    var safeReports = 0
    var unsafeReports = 0
    val problemDampener = true

    // part 1: 383
    // part 2 first try: safe: 564, unsafe: 436 (too high)
    // part 2 second try: safe: 229 unsafe: 771 (too low)
    // part 2 third try: safe: 436 unsafe: 564 (correct)

    File("src/main/resources/day2-input.txt").forEachLine { line ->
        val levels = line.split("\\s+".toRegex()).map { it.toInt() }
        var validSerie = levels.zipWithNext().all { (a, b) -> b > a && b - a in 1..3 } || levels.zipWithNext().all { (a, b) -> a > b && a - b in 1..3 }
        if (problemDampener && !validSerie) {
            validSerie = levels.indices.any { index ->
                val modifiedLevels = levels.toMutableList().apply { removeAt(index) }
                modifiedLevels.zipWithNext().all { (a, b) -> b > a && b - a in 1..3 } || modifiedLevels.zipWithNext().all { (a, b) -> a > b && a - b in 1..3 } }
        }

        if (validSerie) {
            safeReports++
        } else {
            unsafeReports++
        }
    }

    println("safe: $safeReports unsafe: $unsafeReports")
}