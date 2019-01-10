package interactive

class WaterGenerateSpreadOnDirtRule : PatternRule() {
    override val input: Map<Position, CellType> = mapOf(
        (0 to 0) to Water.Down,
        (0 to 1) to Dirt
    )

    override val output: Map<Position, CellType> = mapOf(
        (0 to 0) to Water.Spread
    )
}