package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

class ElectricityRule : Rule(), NamedRule, SerializableRule {
    override val key = Companion.key

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

    object ClearWire : CellType() {
        override fun getColor(x: Int, y: Int) = "#343119"
    }

    override val name = "Electricity"

    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {

        val neighborPositions = listOf(
            position.above,
            position.below,
            position.left,
            position.right
        )

        val directNeighbors = cells.filterKeys { it in neighborPositions }

        // Spread power
        if (cells[position] is Wire && cells.filterKeys { it in neighborPositions }.containsValue(PoweredWire)) {
            return mapOf(position to PoweredWire)
        }

        // Spread clear wire and turn off power on the way
        if (cells[position] is PoweredWire && directNeighbors.containsValue(ClearWire)) {
            return directNeighbors.filterValues { it is ClearWire }.mapValues { Wire } +
                mapOf(position to ClearWire)
        }

        // Remove orphan "clear wire"
        if (cells[position] is ClearWire && !directNeighbors.containsValue(PoweredWire)) {
            return mapOf(position to Wire)
        }
        return emptyMap()
    }
}