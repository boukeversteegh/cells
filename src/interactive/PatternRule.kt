package interactive

typealias PatternMap = Map<Position, CellType>

abstract class PatternRule : Rule() {
    abstract val input: PatternMap
    abstract val output: PatternMap
    abstract val rotatable: Boolean

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val changes = mutableMapOf<Position, CellType>()

        // evaluate normal state
        val patternMatches = input.entries.all { neighbors[position + it.key] == it.value }
        // append changes
        if (patternMatches) {
            changes.putAll(position + output)
        }

        return changes
    }

    companion object {
//        fun patternMatches(patternMap, map) {
//
//        }
    }

    // @todo remember center?
    @JsName("getInput")
    fun getInput(): Array<Array<CellType>> {
        val array = Array(3) { Array<CellType>(3) { Any } }
        input.forEach {
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
