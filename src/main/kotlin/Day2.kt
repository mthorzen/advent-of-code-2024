package com.nordoba

import java.io.File
import kotlin.math.abs

fun main() {
    println("Day 2!")

    var safeReports = 0
    var unsafeReports = 0

    File("src/main/resources/day2-input.txt").forEachLine { line ->
        val levels = line.split("\\s+".toRegex()).map { it.toInt() }
        val differences = levels.zipWithNext().map { (a, b) -> a - b }
        println(levels + " - " + differences)
        if (differences.all { it >= 0 } || differences.all { it <= 0 }) {
            if (differences.all { abs(it) in 1..3 }) {
                println("safe")
                safeReports++
            } else {
                println("unsafe1")
                unsafeReports++
            }
        } else {
            println("unsafe2")
            unsafeReports++
        }
    }

    println("Part 1 safe: $safeReports unsafe: $unsafeReports")
}