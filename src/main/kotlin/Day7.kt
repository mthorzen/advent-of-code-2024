package com.nordoba

import java.io.File

fun main() {
    println("Day 7!")

    val fileContent = File("src/main/resources/day7-input.txt").readText()
    println(fileContent)

    val totalCalibrationResult = fileContent.lines().map { equation ->
        val (testValue, numbers) = equation.split(": ")
        val target = testValue.toLong()
        val numList = numbers.split(" ").map { it.toLong() }
        if (canProduceTarget(target, numList)) target else 0
    }.sum()

    println("Total Calibration Result: $totalCalibrationResult")
}

fun canProduceTarget(target: Long, numbers: List<Long>): Boolean {
    // A helper function to evaluate combinations
    fun evaluate(current: Long, curIndex: Int): Boolean {
        if (curIndex == numbers.size) {
            return current == target
        }

        val nextNumber = numbers[curIndex]

        return evaluate(current + nextNumber, curIndex + 1) ||
                evaluate(current * nextNumber, curIndex + 1)
    }

    return evaluate(numbers[0], 1)
}