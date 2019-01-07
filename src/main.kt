import Cell.*

typealias Position = Pair<Int, Int>

val colors: Map<Cell, String> = mapOf(
    VOID to "#FFFF00",
    NONE to "#000000",
    DIRT to "#4d3d38",
    WATER_DOWN to "#81e6f1",
    WATER_SPREAD to "#31c3df",
    WATER_BOUNCE to "#008ecc",
    WATER_STILL to "#3a6fcf",
    WATER_SOURCE to "#ffffff"
)

@JsName("getColor")
fun getColor(cell: Cell): String {
    return colors[cell] ?: "#FF0000"
}

const val w = 64
const val h = 48
//const val w = 1024
//const val h = 768
typealias World = Array<Array<Cell>>

val world: World = Array(h) { Array(w) { NONE } }

@JsName("get")
fun get(x: Int, y: Int): Cell {
    if (y !in 0 until h || x !in 0 until w) {
        return Cell.VOID
    }
    return world[y][x]
}

@JsName("set")
fun set(x: Int, y: Int, cell: Cell) {
    if (y in 0 until h && x in 0 until w) {
        world[y][x] = cell
    }
}

@JsName("clearWorld")
fun clearWorld() {
    for(y in 0 until h) {
        for(x in 0 until w) {
            world[y][x] = NONE
        }
    }
}

fun clearWater() {
    for (y in 1 until h) {
        for (x in 1 until w) {
            when (world[y][x]) {
                WATER_BOUNCE,
                WATER_DOWN,
                WATER_SPREAD,
                WATER_STILL -> world[y][x] = NONE
            }
        }
    }
}

@JsName("reset")
fun reset() {
    clearWater()
}