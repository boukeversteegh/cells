package interactive

class Layer(private val w: Int, private val h: Int) {
    val cellTypes = mutableListOf<CellType>()
    val rules = mutableListOf<Rule>()
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
            val p = x to y

            val neighborPositions: List<Position> = listOf(p, p.left, p.right, p.below, p.above)
            val neighbors = neighborPositions.map { it -> it to get(it.first, it.second) }.toMap()

            for (rule in rules) {
                changes.putAll(rule.evaluate(p, neighbors))
            }

            // generate down
            if (neighbors[p.above] is Water.Source && c is None) {
                changes[p] = Water.Down
//                    continue
            }

//            // generate down
//            if (c is None && (p.left is Water.Spread || right is Water.Spread) && below !== Dirt) {
//                changes[x to y] = Water.Down
////                    continue
//            }

            // propagate down
            if (neighbors[p.above] is Water.Down && c is None) {
                changes[p] = Water.Down
//                    continue
            }


            // generate spread
            if (c is Water.Down && neighbors[p.below] is Dirt) {
                changes[p] = Water.Spread
//                    continue
            }

            // generate spread
            if (c is Water.Down && neighbors[p.below] is Water.Still) {
                changes[p] = Water.Spread
//                    continue
            }

            // propagate spread
            if (c is None && (neighbors[p.left] is Water.Spread || neighbors[p.right] is Water.Spread) && (neighbors[p.below] is Dirt || neighbors[p.below] is Water.Still)) {
                changes[p] = Water.Spread
//                    continue
            }


            // generate bounce
            if (c is Water.Spread && (neighbors[p.left] is Dirt || neighbors[p.right] is Dirt)) {
                changes[p] = Water.Bounce
//                    continue
            }

            // propagate bounce
            if (c is Water.Spread && (neighbors[p.left] is Water.Bounce || neighbors[p.right] is Water.Bounce)) {
                changes[p] = Water.Bounce
//                    continue
            }


            // generate still
            if (c is Water.Spread && ((neighbors[p.left] is Water.Bounce || neighbors[p.left] is Dirt) && (neighbors[p.right] is Water.Bounce || neighbors[p.right] is Dirt))) {
                changes[p] = Water.Still
//                    continue
            }

            // propagate still
            if (c is Water.Bounce && (neighbors[p.left] is Water.Still || neighbors[p.right] is Water.Still)) {
                changes[p] = Water.Still
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

val Pair<Int, Int>.x: Int
    get() = this.first

val Pair<Int, Int>.y: Int
    get() = this.second

val Pair<Int, Int>.left: Position
    get() = x - 1 to y

val Pair<Int, Int>.above: Position
    get() = x to y - 1

val Pair<Int, Int>.below: Position
    get() = x to y + 1

val Pair<Int, Int>.right: Position
    get() = x + 1 to y
