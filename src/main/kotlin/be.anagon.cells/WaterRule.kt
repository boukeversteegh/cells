package be.anagon.cells

class WaterRule : Rule(), NamedRule {
    override val key = "WaterRule"
    override val name = "WaterRule"

    override fun evaluate(position: Position, cells: Map<Position, CellType>): Map<Position, CellType> {
        val c = cells[position]

        val changes = mutableMapOf<Position, CellType>()
        // propagate spread
        if (c is None && (cells[position.left] is Water.Spread || cells[position.right] is Water.Spread) && (cells[position.below] is Dirt || cells[position.below] is Water.Still)) {
            changes[position] = Water.Spread
        }

        // generate bounce
        if (c is Water.Spread && (cells[position.left] is Dirt || cells[position.right] is Dirt)) {
            changes[position] = Water.Bounce
        }

        // propagate bounce
        if (c is Water.Spread && (cells[position.left] is Water.Bounce || cells[position.right] is Water.Bounce)) {
            changes[position] = Water.Bounce
        }

        // generate still
        if (c is Water.Spread && ((cells[position.left] is Water.Bounce || cells[position.left] is Dirt) && (cells[position.right] is Water.Bounce || cells[position.right] is Dirt))) {
            changes[position] = Water.Still
        }

        // propagate still
        if (c is Water.Bounce && (cells[position.left] is Water.Still || cells[position.right] is Water.Still)) {
            changes[position] = Water.Still
        }

        return changes
    }
}