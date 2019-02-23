package be.anagon.cells

// @todo serialize
class WaterOverflowRule : Rule(), NamedRule {
    override val name = "WaterOverflowRule"
    override val key = "WaterOverflowRule"
    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {
        val c = cells[position]

        val left = cells[position.left]
        val right = cells[position.right]
        val below = cells[position.below]

        if (c is None && (left is Water.Spread || right is Water.Spread) && below !== Dirt) {
            return mapOf(position to Water.Down)
        }
        return emptyMap()
    }
}