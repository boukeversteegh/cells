package be.anagon.cells

class WaterRule : Rule(), NamedRule {
    override val key = "WaterRule"
    override val name = "WaterRule"

    override fun evaluate(position: Position, neighbors: Map<Position, CellType>): Map<Position, CellType> {
        val c = neighbors[position]

        val changes = mutableMapOf<Position, CellType>()
        // propagate spread
        if (c is None && (neighbors[position.left] is Water.Spread || neighbors[position.right] is Water.Spread) && (neighbors[position.below] is Dirt || neighbors[position.below] is Water.Still)) {
            changes[position] = Water.Spread
        }

        // generate bounce
        if (c is Water.Spread && (neighbors[position.left] is Dirt || neighbors[position.right] is Dirt)) {
            changes[position] = Water.Bounce
        }

        // propagate bounce
        if (c is Water.Spread && (neighbors[position.left] is Water.Bounce || neighbors[position.right] is Water.Bounce)) {
            changes[position] = Water.Bounce
        }

        // generate still
        if (c is Water.Spread && ((neighbors[position.left] is Water.Bounce || neighbors[position.left] is Dirt) && (neighbors[position.right] is Water.Bounce || neighbors[position.right] is Dirt))) {
            changes[position] = Water.Still
        }

        // propagate still
        if (c is Water.Bounce && (neighbors[position.left] is Water.Still || neighbors[position.right] is Water.Still)) {
            changes[position] = Water.Still
        }

        return changes
    }
}