package com.nordoba

import java.io.File
import kotlin.math.abs

fun main() {
    println("Day 1!")

    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    //File("src/main/resources/day1-example.txt").forEachLine { line ->
    File("src/main/resources/day1-input.txt").forEachLine { line ->
        val (num1, num2) = line.split("\\s+".toRegex()).map { it.toInt() }
        list1.add(num1)
        list2.add(num2)
    }
    
    list1.sort()
    list2.sort()

    val sumOfDifferences = list1.zip(list2).sumOf { (a, b) -> abs(b - a) }

    println("Sum of differences: $sumOfDifferences")

    // part 1: 2192892
}