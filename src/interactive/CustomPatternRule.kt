package interactive

typealias MutablePatternMap = MutableMap<Position, CellType>

class CustomPatternRule(override val input: MutablePatternMap, override val output: MutablePatternMap) : PatternRule() {

    constructor(input: Map<Position, CellType>, output: Map<Position, CellType>) : this(input.toMutableMap(), output.toMutableMap())

    @JsName("setInput")
    fun setInput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            input.remove(pos(x, y))
        } else {
            input[pos(x, y)] = c
        }
    }

    @JsName("setOutput")
    fun setOutput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            output.remove(pos(x, y))
        } else {
            output[pos(x, y)] = c
        }
    }

    @JsName("getPositions")
    fun getPositions(): Set<Position> {
        return input.keys + output.keys
    }

    @JsName("getMinPosition")
    fun getMinPosition(): Position {
        // todo how to sort by (y, x)?
        val minX = getPositions().map { it.x }.min() ?: 0
        val minY = getPositions().map { it.y }.min() ?: 0
        return pos(minX, minY)
    }

    @JsName("getMaxPosition")
    fun getMaxPosition(): Position {
        // todo how to sort by (y, x)?
        val maxX = getPositions().map { it.x }.max() ?: 0
        val maxY = getPositions().map { it.y }.max() ?: 0
        return pos(maxX, maxY)
    }

    fun getArea(): Array<Array<Position>> {
        val min = getMinPosition()
        val max = getMaxPosition()
        return Array(
            max.y - min.y
        ) { y ->
            Array(max.x - min.x) { x ->
                pos(x, y)
            }
        }
    }

}