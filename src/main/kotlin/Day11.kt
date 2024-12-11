package com.nordoba

fun main() {
    println("Day 11!")

    // Define the initial line of stones
    //val initialLine = listOf(125, 17) // example
    val initialLine: List<Long> = listOf(3935565, 31753, 437818, 7697, 5, 38, 0, 123) // puzzle input


    // Number of blinks (set your desired value here)
    val numberOfBlinks = 25

    // Compute the number of stones after X blinks
    val finalStoneCount = calculateStonesAfterBlinks(initialLine, numberOfBlinks)

    // Output the result
    // part 1 first try: 207683 (correct)
    println("Number of stones after $numberOfBlinks blinks: $finalStoneCount")
}

fun calculateStonesAfterBlinks(initialLine: List<Long>, blinks: Int): Int {
    // Start with the initial line of stones
    var stones = initialLine.toMutableList()

    // Process the stones for the given number of blinks
    repeat(blinks) {
        val newStones = mutableListOf<Long>()
        for (stone in stones) {
            when {
                stone == 0L -> {
                    // Rule 1: Replace 0 with 1
                    newStones.add(1)
                }
                hasEvenDigits(stone) -> {
                    // Rule 2: Split the stone into two stones
                    val parts = splitStone(stone)
                    newStones.addAll(parts)
                }
                else -> {
                    // Rule 3: Multiply the stone by 2024
                    newStones.add(stone * 2024)
                }
            }
        }
        stones = newStones
    }
    // Return the total number of stones after processing
    return stones.size
}

fun hasEvenDigits(number: Long): Boolean {
    return number.toString().length % 2 == 0
}

fun splitStone(number: Long): List<Long> {
    val numberStr = number.toString()
    val len = numberStr.length
    val mid = len / 2

    // Split the number into two halves
    val left = numberStr.substring(0, mid).toLong()
    val right = numberStr.substring(mid, len).toLong()

    return listOf(left, right)
}