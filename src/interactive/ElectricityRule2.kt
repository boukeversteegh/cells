package interactive

import kotlin.js.Json
import kotlin.js.json

class ElectricityRule2 : PatternRule(), NamedRule, SerializableRule {
    override val input: Map<Position, CellType> = mapOf(
        pos(0, 0) to ElectricityRule.Wire,
        pos(0, 1) to ElectricityRule.PoweredWire
    )

    override val output: Map<Position, CellType> = mapOf(
        pos(0, 0) to ElectricityRule.PoweredWire
    )

    override val rotatable = true

    override val key = ElectricityRule2.key

    companion object {
        const val key = "ElectricityRule2"
        fun deserialize() = ElectricityRule2()
    }

    override fun serialize(cellTypes: List<CellType>): Json {
        return json("type" to ElectricityRule2.key)
    }

    override val name = "Electricity 2"

//    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {

//        val neighborPositions = listOf(
//            position.above,
//            position.below,
//            position.left,
//            position.right
//        )
//
//        val directNeighbors = neighbors.filterKeys { it in neighborPositions }
//
//        // Spread power
//        if (neighbors[position] is ElectricityRule.Wire && neighbors.filterKeys { it in neighborPositions }.containsValue(ElectricityRule.PoweredWire)) {
//            return mapOf(position to ElectricityRule.PoweredWire)
//        }
//
//        // Spread clear wire and turn off power on the way
//        if (neighbors[position] is ElectricityRule.PoweredWire && directNeighbors.containsValue(ElectricityRule.ClearWire)) {
//            return directNeighbors.filterValues { it is ElectricityRule.ClearWire }.mapValues { ElectricityRule.Wire } +
//                mapOf(position to ElectricityRule.ClearWire)
//        }
//
//        // Remove orphan "clear wire"
//        if (neighbors[position] is ElectricityRule.ClearWire && !directNeighbors.containsValue(ElectricityRule.PoweredWire)) {
//            return mapOf(position to ElectricityRule.Wire)
//        }
//        return emptyMap()
//    }
}