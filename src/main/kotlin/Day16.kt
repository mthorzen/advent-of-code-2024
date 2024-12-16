package com.nordoba

import java.io.File
import java.util.PriorityQueue

// Directions constants
enum class Day16Direction {
    EAST, SOUTH, WEST, NORTH;

    // Rotate right (clockwise)
    fun rotateRight(): Day16Direction {
        return values()[(this.ordinal + 1) % 4]
    }

    // Rotate left (counterclockwise)
    fun rotateLeft(): Day16Direction {
        return values()[(this.ordinal + 3) % 4]
    }
}

// Coordinate data class
data class State(val x: Int, val y: Int, val direction: Day16Direction, val score: Int): Comparable<State> {
    override fun compareTo(other: State): Int {
        return this.score - other.score
    }
}

fun lowestScoreMaze(mapInput: List<String>): Int {
    val map = mapInput.map { it.toCharArray() } // Convert input into 2D char array

    // Find start and end positions
    var startX = 0
    var startY = 0
    var endX = 0
    var endY = 0

    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == 'S') {
                startX = i
                startY = j
            }
            if (map[i][j] == 'E') {
                endX = i
                endY = j
            }
        }
    }

    // Directions array to calculate forward movement [dx, dy]
    val directions = listOf(
        Pair(0, 1),  // EAST
        Pair(1, 0),  // SOUTH
        Pair(0, -1), // WEST
        Pair(-1, 0)  // NORTH
    )

    // Priority queue for A* Search (sorted by score)
    val queue = PriorityQueue<State>()
    // Starting state
    queue.add(State(startX, startY, Day16Direction.EAST, 0))

    // Visited states with minimum scores to avoid revisiting
    val visited = mutableMapOf<Triple<Int, Int, Day16Direction>, Int>()

    // A* Search
    while (queue.isNotEmpty()) {
        val current = queue.poll()

        // If we've reached the end, return the score
        if (current.x == endX && current.y == endY) {
            return current.score
        }

        // Check if this state has been visited with a lower score
        val stateKey = Triple(current.x, current.y, current.direction)
        if (visited[stateKey] != null && visited[stateKey]!! <= current.score) {
            continue
        }
        visited[stateKey] = current.score

        // Try moving forward
        val (dx, dy) = directions[current.direction.ordinal]
        val newX = current.x + dx
        val newY = current.y + dy
        if (map[newX][newY] != '#') { // Valid movement
            queue.add(State(newX, newY, current.direction, current.score + 1))
        }

        // Try rotating clockwise (1000 penalty)
        val rightRotated = current.direction.rotateRight()
        queue.add(State(current.x, current.y, rightRotated, current.score + 1000))

        // Try rotating counterclockwise (1000 penalty)
        val leftRotated = current.direction.rotateLeft()
        queue.add(State(current.x, current.y, leftRotated, current.score + 1000))
    }

    // If no path is found, return -1 (shouldn't happen with a valid map)
    return -1
}

// Test with the given examples
fun main() {
    println("Day 16!")
    val map1 = File("src/main/resources/day16-example1.txt").readLines()
    val map2 = File("src/main/resources/day16-example2.txt").readLines()
    val map3 = File("src/main/resources/day16-input.txt").readLines()

    println(lowestScoreMaze(map1)) // Should print: 7036
    println(lowestScoreMaze(map2)) // Should print: 11048
    println(lowestScoreMaze(map3))
}