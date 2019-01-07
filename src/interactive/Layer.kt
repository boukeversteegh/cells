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
}