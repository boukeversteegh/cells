package day17
import Cell.*
import Cell
import Position
import World
import clearWorld
import get
import set


// initialize empty world
@JsName("loadWorld")
fun loadWorld(input: Array<String>) {
    clearWorld()
    for (y in input.indices) {
        for (x in input[y].indices) {
            when (input[y].get(x)) {
                '#' -> set(x, y, DIRT)
                '+' -> set(x, y, WATER_SOURCE)
            }
        }
    }
}

val lastChanges = mutableSetOf<Position>()

@JsName("iterate")
fun iterate(world: World, w: Int, h: Int): Array<Position> {
    val positions = mutableSetOf<Position>()


    lastChanges.flatMapTo(positions) {
        val (x, y) = it

        listOf(
            x to y - 1,
            x - 1 to y, it, x + 1 to y,
            x to y + 1
        )
    }

    if (positions.isEmpty()) {
        positions.add(w / 2 to 1)
    }

    val changes = mutableMapOf<Position, Cell>()
    for ((x, y) in positions) {
        val c = get(x, y)
        val left = get(x - 1, y)
        val right = get(x + 1, y)
        val below = get(x, y + 1)
        val above = get(x, y - 1)

        // generate down
        if (above === WATER_SOURCE && c === NONE) {
            changes[x to y] = WATER_DOWN
        }

        // generate down
        if (c === NONE && (left === WATER_SPREAD || right === WATER_SPREAD) && below !== DIRT) {
            changes[x to y] = WATER_DOWN
        }

        // propagate down
        if (above === WATER_DOWN && c === NONE) {
            changes[x to y] = WATER_DOWN
        }


        // generate spread
        if (c === WATER_DOWN && below === DIRT) {
            changes[x to y] = WATER_SPREAD
        }

        // generate spread
        if (c === WATER_DOWN && below === WATER_STILL) {
            changes[x to y] = WATER_SPREAD
        }

        // propagate spread
        if (c === NONE && (left === WATER_SPREAD || right === WATER_SPREAD) && (below === DIRT || below === WATER_STILL)) {
            changes[x to y] = WATER_SPREAD
        }


        // generate bounce
        if (c === WATER_SPREAD && (left === DIRT || right === DIRT)) {
            changes[x to y] = WATER_BOUNCE
        }

        // propagate bounce
        if (c === WATER_SPREAD && (left === WATER_BOUNCE || right === WATER_BOUNCE)) {
            changes[x to y] = WATER_BOUNCE
        }


        // generate still
        if (c === WATER_SPREAD && ((left === WATER_BOUNCE || left === DIRT) && (right === WATER_BOUNCE || right === DIRT))) {
            changes[x to y] = WATER_STILL
        }

        // propagate still
        if (c === WATER_BOUNCE && (left === WATER_STILL || right === WATER_STILL)) {
            changes[x to y] = WATER_STILL
        }
    }

    for ((pos, cell) in changes) {
        val (x, y) = pos
        set(x, y, cell)
    }
    lastChanges.clear()
    lastChanges.addAll(changes.keys)
    return changes.keys.toTypedArray()
}
