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
        if (y in 0 until h && x in 0 until w && cellType !is Any) {
            cells[y][x] = cellType
        }
    }

    private fun get(position: Position): CellType {
        return this.get(position.x, position.y)
    }

    private fun get(positions: List<Position>): Map<Position, CellType> {
        return positions.map { it -> it to get(it) }.toMap()
    }

    @JsName("setByIndex")
    fun set(x: Int, y: Int, cellTypeIndex: Int) {
        set(x, y, cellTypes[cellTypeIndex])
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

    @JsName("getRules")
    fun getRules(): Array<Rule> {
        return rules.toTypedArray()
    }

    @JsName("addRule")
    fun addRule() {
        rules.add(CustomPatternRule(emptyMap(), emptyMap()))
    }

    @JsName("addCellType")
    fun addCellType(color: String = "#FF00FF") {
        cellTypes.add(CustomCellType(color))
    }

    fun iterate(lastChangedPositions: List<Position>) {
        val changes = mutableMapOf<Position, CellType>()
        for ((x, y) in lastChangedPositions) {
            val c = get(x, y)
            val p = x to y

            val neighborPositions: List<Position> = listOf(
                p,
                p.left,
                p.right,
                p.below,
                p.above,
                (x - 1 to y - 1),
                (x - 1 to y + 1),
                (x + 1 to y - 1),
                (x + 1 to y + 1)
            )
            val neighbors = get(neighborPositions)

            for (rule in rules) {
                changes.putAll(rule.evaluate(p, neighbors))
            }

            // propagate spread
            if (c is None && (neighbors[p.left] is Water.Spread || neighbors[p.right] is Water.Spread) && (neighbors[p.below] is Dirt || neighbors[p.below] is Water.Still)) {
                changes[p] = Water.Spread
            }

            // generate bounce
            if (c is Water.Spread && (neighbors[p.left] is Dirt || neighbors[p.right] is Dirt)) {
                changes[p] = Water.Bounce
            }

            // propagate bounce
            if (c is Water.Spread && (neighbors[p.left] is Water.Bounce || neighbors[p.right] is Water.Bounce)) {
                changes[p] = Water.Bounce
            }


            // generate still
            if (c is Water.Spread && ((neighbors[p.left] is Water.Bounce || neighbors[p.left] is Dirt) && (neighbors[p.right] is Water.Bounce || neighbors[p.right] is Dirt))) {
                changes[p] = Water.Still
            }

            // propagate still
            if (c is Water.Bounce && (neighbors[p.left] is Water.Still || neighbors[p.right] is Water.Still)) {
                changes[p] = Water.Still
            }
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
