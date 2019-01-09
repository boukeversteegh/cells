package interactive

class Layer(private val w: Int, private val h: Int) {
    val cellTypes = mutableListOf<CellType>()
    private val cells = Array(h) { Array<CellType>(w) { None } }

    @JsName("get")
    fun get(x: Int, y: Int): CellType {
        if (y !in 0 until h || x !in 0 until w) {
            return Void
        }
        return cells[y][x]
    }

    @JsName("set")
    fun set(x: Int, y: Int, cellType: CellType) {
        if (y in 0 until h && x in 0 until w) {
            cells[y][x] = cellType
        }
    }

    @JsName("setByIndex")
    fun set(x: Int, y: Int, cellTypeIndex: Int) {
        if (y in 0 until h && x in 0 until w) {
            cells[y][x] = cellTypes[cellTypeIndex]
        }
    }

    @JsName("clear")
    fun clear() {
        for (y in 0 until h) {
            for (x in 0 until w) {
                cells[y][x] = None
            }
        }
    }

    @JsName("getCellTypes")
    fun getCellTypes(): Array<CellType> {
        return cellTypes.toTypedArray()
    }

    fun iterate(lastChangedPositions: List<Position>) {
        val changes = mutableMapOf<Position, CellType>()
        for ((x, y) in lastChangedPositions) {
            val c = get(x, y)
            val left = get(x - 1, y)
            val right = get(x + 1, y)
            val below = get(x, y + 1)
            val above = get(x, y - 1)

            // generate down
            if (above is Water.Source && c is None) {
                changes[x to y] = Water.Down
//                    continue
            }

            // generate down
            if (c is None && (left is Water.Spread || right is Water.Spread) && below !== Dirt) {
                changes[x to y] = Water.Down
//                    continue
            }

            // propagate down
            if (above is Water.Down && c is None) {
                changes[x to y] = Water.Down
//                    continue
            }


            // generate spread
            if (c is Water.Down && below is Dirt) {
                changes[x to y] = Water.Spread
//                    continue
            }

            // generate spread
            if (c is Water.Down && below is Water.Still) {
                changes[x to y] = Water.Spread
//                    continue
            }

            // propagate spread
            if (c is None && (left is Water.Spread || right is Water.Spread) && (below is Dirt || below is Water.Still)) {
                changes[x to y] = Water.Spread
//                    continue
            }


            // generate bounce
            if (c is Water.Spread && (left is Dirt || right is Dirt)) {
                changes[x to y] = Water.Bounce
//                    continue
            }

            // propagate bounce
            if (c is Water.Spread && (left is Water.Bounce || right is Water.Bounce)) {
                changes[x to y] = Water.Bounce
//                    continue
            }


            // generate still
            if (c is Water.Spread && ((left is Water.Bounce || left is Dirt) && (right is Water.Bounce || right is Dirt))) {
                changes[x to y] = Water.Still
//                    continue
            }

            // propagate still
            if (c is Water.Bounce && (left is Water.Still || right is Water.Still)) {
                changes[x to y] = Water.Still
//                    continue
            }


//                // remove down
//                if (c is Water.Down && !(above is Water || left is Water || right is Water)) {
//                    changes[x to y] = None
//                    continue
//                }
        }

        for ((pos, cell) in changes) {
            val (x, y) = pos
            set(x, y, cell)
        }
    }
}