package com.nordoba
import java.io.File

data class Position(var x: Int, var y: Int) {
    fun move(direction: Direction) {
        when (direction) {
            Direction.UP -> y--
            Direction.RIGHT -> x++
            Direction.DOWN -> y++
            Direction.LEFT -> x--
        }
    }
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun turnRight(): Direction {
        return values()[(ordinal + 1) % values().size]
    }
}

fun loadMap(filePath: String): List<CharArray> {
    return File(filePath).readLines().map { it.toCharArray() }
}

fun printMap(map: List<CharArray>) {
    map.forEach { println(it.concatToString()) }
}

fun isWithinBounds(pos: Position, maxX: Int, maxY: Int): Boolean {
    return pos.x in 0..maxX && pos.y in 0..maxY
}

fun main() {
    val filePath = "src/main/resources/day6-input.txt"
    val map = loadMap(filePath)
    val maxX = map[0].size - 1
    val maxY = map.size - 1

    var direction = Direction.UP
    val visited = mutableSetOf<Position>()
    var currentPosition: Position? = null

    // Find the initial position and direction
    for (y in map.indices) {
        for (x in map[y].indices) {
            if (map[y][x] in listOf('^', '>', 'v', '<')) {
                currentPosition = Position(x, y)
                direction = when (map[y][x]) {
                    '^' -> Direction.UP
                    '>' -> Direction.RIGHT
                    'v' -> Direction.DOWN
                    '<' -> Direction.LEFT
                    else -> throw IllegalStateException("Unexpected symbol on map")
                }
                map[y][x] = '.'
                break
            }
        }
        if (currentPosition != null) break
    }

    // Set to track visited states for loop detection
    val stateHistory = mutableSetOf<Pair<Position, Direction>>()

    // Simulate the guard's movements
    var loopDetected = false

    while (currentPosition != null && isWithinBounds(currentPosition, maxX, maxY)) {
        // Mark as visited
        println("Current position: $currentPosition")
        visited.add(Position(currentPosition.x, currentPosition.y))

        // Save the current state
        val currentState = Pair(Position(currentPosition.x, currentPosition.y), direction)

        // Check for loop by comparing the current state with history
        if (currentState in stateHistory) {
            println("Loop detected! Patrol aborted.")
            loopDetected = true
            break
        }
        stateHistory.add(currentState)

        // Check what's in front
        val nextPosition = Position(currentPosition.x, currentPosition.y)
        nextPosition.move(direction)
        println("Next position: $nextPosition")

        if (!isWithinBounds(nextPosition, maxX, maxY)) {
            break;
        }

        if (map[nextPosition.y][nextPosition.x] == '#') {
            // Turn right
            direction = direction.turnRight()
        } else {
            // Move forward
            currentPosition.move(direction)
        }
    }

    if (!loopDetected) {
        // Mark visited positions on the map
        visited.forEach { pos ->
            if (isWithinBounds(pos, maxX, maxY)) {
                map[pos.y][pos.x] = 'X'
            }
        }

        // Print the final map and visited count
        printMap(map)
        println("Visited positions count: ${visited.size}")

        // Part 1 first try: 4696 (correct)
    }
}