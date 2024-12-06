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
        return entries[(ordinal + 1) % entries.size]
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

fun identifyPotentialLoopPositions(filePath: String): Int {
    val map = loadMap(filePath)
    val maxX = map[0].size - 1
    val maxY = map.size - 1

    var startPosition: Position? = null
    var startDirection = Direction.UP

    // Find the initial position and direction of the guard
    for (y in map.indices) {
        for (x in map[y].indices) {
            if (map[y][x] in listOf('^', '>', 'v', '<')) {
                startPosition = Position(x, y)
                startDirection = when (map[y][x]) {
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
        if (startPosition != null) break
    }

    if (startPosition == null) {
        throw IllegalStateException("No starting position found on the map.")
    }

    println("Start position: $startPosition")

    val potentialPositions = mutableSetOf<Position>()

    // Iterate over each position that can potentially hold an obstruction
    for (y in map.indices) {
        for (x in map[y].indices) {
            if (map[y][x] == '.' && !(x == startPosition.x && y == startPosition.y)) {
                val testMap = map.map { it.copyOf() }
                testMap[y][x] = '#'

                if (causesLoop(testMap, startPosition, startDirection, maxX, maxY)) {
                    potentialPositions.add(Position(x, y))
                }
            }
        }
    }

    return potentialPositions.size
}

fun causesLoop(
    testMap: List<CharArray>,
    startPosition: Position,
    startDirection: Direction,
    maxX: Int,
    maxY: Int
): Boolean {
    var currentPosition = Position(startPosition.x, startPosition.y)
    var direction = startDirection
    val stateHistory = mutableSetOf<Pair<Position, Direction>>()

    while (isWithinBounds(currentPosition, maxX, maxY)) {
        val currentState = Pair(Position(currentPosition.x, currentPosition.y), direction)
        if (currentState in stateHistory) {
            return true
        }
        stateHistory.add(currentState)

        val nextPosition = Position(currentPosition.x, currentPosition.y)
        nextPosition.move(direction)

        if (!isWithinBounds(nextPosition, maxX, maxY)) {
            return false;
        }

        if (testMap[nextPosition.y][nextPosition.x] == '#') {
            direction = direction.turnRight()
        } else {
            currentPosition.move(direction)
        }
    }

    return false
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
        visited.add(Position(currentPosition.x, currentPosition.y))

        // Save the current state
        val currentState = Pair(Position(currentPosition.x, currentPosition.y), direction)

        // Check for loop by comparing the current state with history
        if (currentState in stateHistory) {
            loopDetected = true
            break
        }
        stateHistory.add(currentState)

        // Check what's in front
        val nextPosition = Position(currentPosition.x, currentPosition.y)
        nextPosition.move(direction)

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
        // Part 1 first try: 4696 (correct)
        println("Visited positions count: ${visited.size}")
    }

    // part 2: first try 16080 (too high)
    // part 2: secind try 1443 (correct)
    val obstructionPositions = identifyPotentialLoopPositions(filePath)
    println("Number of potential obstruction positions: $obstructionPositions")
}


