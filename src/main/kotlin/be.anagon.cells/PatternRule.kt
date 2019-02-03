package be.anagon.cells

typealias PatternMap = Map<Position, CellType>

abstract class PatternRule : Rule() {
    abstract val input: PatternMap
    abstract val output: PatternMap

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val patternMatches = input.entries.all { neighbors[position + it.key] == it.value }

        return if (patternMatches) (position + output) else emptyMap()
    }

    // @todo remember center?
    @JsName("getInput")
    fun getInput(): Array<Array<CellType>> {
        val array = Array(3) { Array<CellType>(3) { Any } }
        input.forEach { it ->
            val position = it.key
            val cellType = it.value
            array[position.y + 1][position.x + 1] = cellType
        }
        return array
    }

    @JsName("getInputCellType")
    fun getInput(x: Int, y: Int): CellType {
        return input.getOrElse(pos(x, y)) { Any }
    }

    @JsName("getOutputCellType")
    fun getOutput(x: Int, y: Int): CellType {
        return output.getOrElse(pos(x, y)) { Any }
    }
}

private operator fun <V> Position.plus(map: Map<Position, V>): Map<Position, V> {
    return map.map { it ->
        (this + it.key) to it.value
    }.toMap()
}
