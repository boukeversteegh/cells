package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

class RandomWalkRule : Rule(), NamedRule, SerializableRule {
    var cellType: CellType = Any
    var background: CellType = None

    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {
        throw Exception("Should not run")
    }

    override fun evaluate(position: Position, cells: Map<Position, CellType>, changes: MutableMap<Position, CellType>) {
        if (cellType != Any && cells[position] == cellType) {
            val randomDirection = listOf(position.above, position.below, position.left, position.right).random()
            if (cells[randomDirection] == background) {
                changes[position] = background
                changes[randomDirection] = cellType
                return
            }
            changes[position] = cellType
        }
    }

    override var name: String = "RandomWalk"

    override val key = Companion.key

    companion object {
        const val key = "RandomWalkRule"
        fun deserialize() = new()

        @JsName("new")
        fun new() = RandomWalkRule()
    }

    override fun serialize(cellTypes: List<CellType>): Json {
        return json("type" to key)
    }
}