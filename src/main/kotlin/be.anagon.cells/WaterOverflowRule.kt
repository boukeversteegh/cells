package be.anagon.cells

// @todo serialize
class WaterOverflowRule : Rule(), NamedRule {
    override val name = "WaterOverflowRule"
    override val key = "WaterOverflowRule"
    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val c = neighbors[position]

        val left = neighbors[position.left]
        val right = neighbors[position.right]
        val below = neighbors[position.below]

        if (c is None && (left is Water.Spread || right is Water.Spread) && below !== Dirt) {
            return mapOf(position to Water.Down)
        }
        return emptyMap()
    }
}