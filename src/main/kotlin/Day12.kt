package com.nordoba

import java.io.File

fun calculateTotalFencingCost(map: List<String>): Int {
    val rows = map.size
    val cols = map[0].length
    val visited = Array(rows) { BooleanArray(cols) }
    var totalCost = 0

    val directions = listOf(
        Pair(-1, 0), // Up
        Pair(1, 0),  // Down
        Pair(0, -1), // Left
        Pair(0, 1)   // Right
    )

    // Helper function to check if a cell is valid and belongs to the same region
    fun isValid(x: Int, y: Int, type: Char): Boolean {
        return x in 0 until rows &&
                y in 0 until cols &&
                !visited[x][y] &&
                map[x][y] == type
    }

    // BFS/DFS to explore a region and calculate its area and perimeter
    fun exploreRegion(x: Int, y: Int): Pair<Int, Int> {
        val queue = mutableListOf(Pair(x, y))
        visited[x][y] = true
        val type = map[x][y]
        var area = 0
        var perimeter = 0

        while (queue.isNotEmpty()) {
            val (cx, cy) = queue.removeAt(0)
            area++

            for ((dx, dy) in directions) {
                val nx = cx + dx
                val ny = cy + dy
                if (isValid(nx, ny, type)) {
                    visited[nx][ny] = true
                    queue.add(Pair(nx, ny))
                } else if (nx !in 0 until rows || ny !in 0 until cols || map.getOrNull(nx)?.getOrNull(ny) != type) {
                    perimeter++ // Count edge of the plot as perimeter
                }
            }
        }

        return Pair(area, perimeter)
    }

    // Main loop to explore the entire map
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (!visited[i][j]) {
                val (area, perimeter) = exploreRegion(i, j)
                totalCost += area * perimeter
            }
        }
    }

    return totalCost
}

fun main() {
    val fileContent = File("src/main/resources/day12-input.txt").readText()
    println(fileContent)
    
    val map = fileContent.lines()
    
    val totalCost = calculateTotalFencingCost(map)
    println("Total fencing cost: $totalCost")
}