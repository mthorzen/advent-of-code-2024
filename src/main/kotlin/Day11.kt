package com.nordoba

import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    println("Day 11!")

    // Define the initial line of stones
    //val initialLine = listOf(125, 17) // example
    val initialLine: List<Long> = listOf(3935565, 31753, 437818, 7697, 5, 38, 0, 123) // puzzle input


    // Number of blinks (set your desired value here)
    val numberOfBlinks = 75

    // Compute the number of stones after X blinks
    val finalStoneCount = calculateStonesAfterBlinks(initialLine, numberOfBlinks)

    // Output the result
    // part 1 first try: 207683 (correct)
    // part 2 : 244782991106220 (correct)
    println("Number of stones after $numberOfBlinks blinks: $finalStoneCount")
}
fun calculateStonesAfterBlinks(initialLine: List<Long>, blinks: Int): BigInteger {
    // Replace counts with BigDecimal for safer accumulation
    var stoneCounts = initialLine.map { it.toBigInteger() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toBigDecimal() } // Convert count to BigDecimal
        .toMutableMap()

    repeat(blinks) { blink ->
        println("Blink $blink: Total stones = ${stoneCounts.values.fold(BigDecimal.ZERO, BigDecimal::add)}")
        val newStoneCounts = mutableMapOf<BigInteger, BigDecimal>()

        for ((stone, count) in stoneCounts) {
            when {
                stone == BigInteger.ZERO -> {
                    // Rule 1: Replace 0 with 1
                    newStoneCounts[BigInteger.ONE] =
                        newStoneCounts.getOrDefault(BigInteger.ONE, BigDecimal.ZERO).add(count)
                }
                hasEvenDigits(stone) -> {
                    // Rule 2: Split the stone into two parts
                    val parts = splitStone(stone)
                    for (part in parts) {
                        newStoneCounts[part] =
                            newStoneCounts.getOrDefault(part, BigDecimal.ZERO).add(count)
                    }
                }
                else -> {
                    // Rule 3: Multiply the stone by 2024
                    val newStone = stone * BigInteger.valueOf(2024)
                    newStoneCounts[newStone] =
                        newStoneCounts.getOrDefault(newStone, BigDecimal.ZERO).add(count)
                }
            }
        }

        // Detect any negative numbers in the counts
        for ((stone, count) in newStoneCounts) {
            require(count >= BigDecimal.ZERO) {
                "Error: Negative count detected for stone $stone after computations -> $count"
            }
        }
        stoneCounts = newStoneCounts
    }

    // Sum up all counts to get the number of stones after the given number of blinks
    return stoneCounts.values.fold(BigDecimal.ZERO, BigDecimal::add).toBigInteger()
}

fun splitStone(number: BigInteger): List<BigInteger> {
    if (number < BigInteger.TEN) return listOf(number)

    val numberStr = number.toString()
    val len = numberStr.length
    val mid = len / 2

    val left = numberStr.substring(0, mid).toBigInteger()
    val right = numberStr.substring(mid, len).toBigInteger()

    return listOf(left, right)
}

fun hasEvenDigits(number: BigInteger): Boolean {
    return number.toString().length % 2 == 0
}
