package interactive

typealias Position = Pair<Int, Int>


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

    fun lastChangedPositions(): List<Position> {
        return (0 until h).toList().flatMap { y ->
            (0 until w).map { x -> x to y }
        }
    }

    fun iterate() {
        layers[0].run {
            val positions = lastChangedPositions()

            val changes = mutableMapOf<Position, CellType>()
            for ((x, y) in positions) {
                val c = get(x, y)
                val left = get(x - 1, y)
                val right = get(x + 1, y)
                val below = get(x, y + 1)
                val above = get(x, y - 1)

                // generate down
                if (above is Water.Source && c is None) {
                    changes[x to y] = Water.Down
                    continue
                }

                // generate down
                if (c is None && (left is Water.Spread || right is Water.Spread) && below !== Dirt) {
                    changes[x to y] = Water.Down
                    continue
                }

                // propagate down
                if (above is Water.Down && c is None) {
                    changes[x to y] = Water.Down
                    continue
                }


                // generate spread
                if (c is Water.Down && below is Dirt) {
                    changes[x to y] = Water.Spread
                    continue
                }

                // generate spread
                if (c is Water.Down && below is Water.Still) {
                    changes[x to y] = Water.Spread
                    continue
                }

                // propagate spread
                if (c is None && (left is Water.Spread || right is Water.Spread) && (below is Dirt || below is Water.Still)) {
                    changes[x to y] = Water.Spread
                    continue
                }


                // generate bounce
                if (c is Water.Spread && (left is Dirt || right is Dirt)) {
                    changes[x to y] = Water.Bounce
                    continue
                }

                // propagate bounce
                if (c is Water.Spread && (left is Water.Bounce || right is Water.Bounce)) {
                    changes[x to y] = Water.Bounce
                    continue
                }


                // generate still
                if (c is Water.Spread && ((left is Water.Bounce || left is Dirt) && (right is Water.Bounce || right is Dirt))) {
                    changes[x to y] = Water.Still
                    continue
                }

                // propagate still
                if (c is Water.Bounce && (left is Water.Still || right is Water.Still)) {
                    changes[x to y] = Water.Still
                    continue
                }


                // remove down
                if (c is Water.Down && !(above is Water || left is Water || right is Water)) {
                    changes[x to y] = None
                    continue
                }
            }

            for ((pos, cell) in changes) {
                val (x, y) = pos
                set(x, y, cell)
            }
        }
    }
}