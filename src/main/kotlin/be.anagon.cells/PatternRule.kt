package be.anagon.cells

typealias PatternMap = Map<Position, CellType>

fun PatternMap.rotateRight(): PatternMap {
    return entries.map {
        /*
         A
         +

         *A

         */
        val (pos, cellType) = it
        Position(x = -pos.y, y = pos.x) to cellType
    }.toMap()
}

private fun PatternMap.relativeTo(position: Position): PatternMap {
    return map {
        (it.key - position) to it.value
    }.toMap()
}

private fun PatternMap.matches(otherMap: Map<Position, CellType>): Boolean {
    return all { it.value == otherMap[it.key] }
}

abstract class PatternRule : Rule() {
    abstract val input: PatternMap
    abstract val output: PatternMap
    abstract val rotatable: Boolean

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val changes = mutableMapOf<Position, CellType>()

        val relativeNeighbors = neighbors.relativeTo(position)

        if (input.isNotEmpty()) {
            if (input.matches(relativeNeighbors)) {
                changes.putAll(position + output)
            }

            if (rotatable) {
                var rotatedInput = input
                var rotatedOutput = output
                repeat(3) {
                    rotatedInput = rotatedInput.rotateRight()
                    rotatedOutput = rotatedOutput.rotateRight()
                    if (rotatedInput.matches(relativeNeighbors)) {
                        changes.putAll(position + rotatedOutput)
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
