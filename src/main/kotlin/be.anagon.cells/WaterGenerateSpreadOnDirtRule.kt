package be.anagon.cells

class WaterGenerateSpreadOnDirtRule : PatternRule() {
    override val input: Map<Position, CellType> = mapOf(
        pos(0, 0) to Water.Down,
        pos(0, 1) to Dirt
    )

    override val output: Map<Position, CellType> = mapOf(
        pos(0, 0) to Water.Spread
    )
}