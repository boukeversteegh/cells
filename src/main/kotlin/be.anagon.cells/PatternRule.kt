package be.anagon.cells

typealias PatternMap = Map<Position, CellType>

fun PatternMap.rotateRight(): PatternMap {
    return entries.map {
        val (pos, cellType) = it
        Position(x = -pos.y, y = pos.x) to cellType
    }.toMap()
}

private fun PatternMap.matches(position: Position, otherMap: Map<Position, CellType>): Boolean {
    return isNotEmpty() && all {
        it.value == otherMap[it.key + position]
    }
}

abstract class PatternRule : Rule() {
    abstract val input: PatternMap
    abstract val output: PatternMap
    abstract val rotatable: Boolean

    open val rotatedInputs: Array<PatternMap> = Array(3) { emptyMap<Position, CellType>() }
    open val rotatedOutputs: Array<PatternMap> = Array(3) { emptyMap<Position, CellType>() }

    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {
        val changes = mutableMapOf<Position, CellType>()

        if (input.isNotEmpty()) {
            if (input.matches(position, cells)) {
                changes.putAll(position + output)
            }

            if (rotatable) {
                for (i in (0..2)) {
                    if ((rotatedInputs[i]).matches(position, cells)) {
                        changes.putAll(position + rotatedOutputs[i])
                    }
                }
            }
        }

        return changes
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
