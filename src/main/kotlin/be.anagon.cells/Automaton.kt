package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

data class Position(val x: Int, val y: Int) {
    operator fun plus(position: Position): Position {
        return Position(x + position.x, y + position.y)
    }

    operator fun minus(position: Position): Position {
        return Position(x - position.x, y - position.y)
    }

    fun serialize(): Json = json("x" to x, "y" to y)
}

fun pos(x: Int, y: Int): Position {
    return Position(x, y)
}

class Automaton(val w: Int, val h: Int) {
    val layers = mutableListOf<Layer>()

    @JsName("getColor")
    fun getColor(x: Int, y: Int): Color {
        val cell = layers.map { it.get(x, y) }
            .firstOrNull { it !is None && it !is Void }

        return (cell ?: None).getColor(x, y)
    }

    fun addLayer(): Layer {
        val layer = Layer(w, h)
        layers.add(layer)
        return layer
    }


    @JsName("getLayers")
    fun getLayers(): Array<Layer> {
        return layers.toTypedArray()
    }

    fun iterate() {
        layers[0].iterate()
    }
}