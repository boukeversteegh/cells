package interactive

import kotlin.js.Json
import kotlin.js.json

class ElectricityRule : Rule(), NamedRule, SerializableRule {
    override val key = ElectricityRule.key

    companion object {
        const val key = "ElectricityRule"
        fun deserialize() = ElectricityRule()
    }

    override fun serialize(cellTypes: List<CellType>): Json {
        return json("type" to "ElectricityRule")
    }

    object Wire : CellType() {
        override fun getColor(x: Int, y: Int) = "#5F5C26"
    }

    object PoweredWire : CellType() {
        override fun getColor(x: Int, y: Int) = "#A6C449"
    }

    override val name = "Electricity"

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {

        val directNeighbors = listOf(
            position.above,
            position.below,
            position.left,
            position.right
        )

        val hasPoweredNeighbor = neighbors.filterKeys { it in directNeighbors }.containsValue(PoweredWire)

        if (neighbors[position] is Wire && hasPoweredNeighbor) {
            return mapOf(position to PoweredWire)
        }
        return emptyMap()
    }
}