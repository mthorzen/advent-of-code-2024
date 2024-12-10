package com.nordoba

import java.io.File

data class Trail(val startPos: String, val path: List<Pair<Int, Int>>)

fun findTrails(map: List<List<Int>>): List<Trail> {
    val trails = mutableListOf<Trail>()
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    fun hasDuplicateTrail(startPos: String, endPoint: Pair<Int, Int>): Boolean {
        return trails.any { it.startPos == startPos && it.path.last() == endPoint }
    }

    fun dfs(startPos: String, x: Int, y: Int, path: List<Pair<Int, Int>>) {
        if (map[x][y] == 9) {
            val newPath = path + Pair(x, y)
            // part 1
            /*if (!hasDuplicateTrail(startPos, newPath.last())) {
                trails.add(Trail(startPos, newPath))
            }*/
            // part 2
            trails.add(Trail(startPos, newPath))
            return
        }

        for (direction in directions) {
            val newX = x + direction.first
            val newY = y + direction.second

            if (newX in map.indices && newY in map[0].indices &&
                map[newX][newY] == map[x][y] + 1 &&
                !path.contains(Pair(newX, newY))
            ) {
                dfs(startPos, newX, newY, path + Pair(x, y))
            }
        }
    }

    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == 0) {
                println("Starting at ($i, $j)")
                dfs("x=$i,y=$j", i, j, emptyList())
            }
        }
    }

    return trails
}

fun main() {
    println("Day 10!")

    val fileContent = File("src/main/resources/day10-input.txt").readText()
    println(fileContent)

    val map = fileContent.lines().map { line -> line.map { it.toString().toInt() } }
    println(map)

    val trails = findTrails(map)

    // Part 1 first try: 514 (correct)
    // part 2 first try: 1162 (correct)
    trails.forEach { trail -> println(trail) }
    println("Sum: " + trails.size)
}
