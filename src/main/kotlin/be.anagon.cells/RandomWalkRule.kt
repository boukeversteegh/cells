package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

class RandomWalkRule : Rule(), NamedRule, SerializableRule {
    var cellType: CellType = Any
    var background: CellType = None

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        if (cellType != Any && neighbors[position] == cellType) {
            val randomDirection = listOf(position.above, position.below, position.left, position.right).random()
            if (neighbors[randomDirection] == background) {
                return mapOf(
                    position to background,
                    randomDirection to cellType
                )
            }
            return mapOf(position to cellType)
        }

        return emptyMap()
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