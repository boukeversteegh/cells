package interactive

class CustomPatternRule(override val input: Map<Position, CellType>, override val output: Map<Position, CellType>) : PatternRule() {
    constructor(
        inPattern: List<List<CellType>>,
        outPattern: List<List<CellType>>,
        center: Position = 1 to 1
    ) :
        this(inPattern.toPositionMap(center), outPattern.toPositionMap(center))

    constructor(
        inPattern: List<List<CellType>>,
        outCellType: CellType,
        center: Position
    ) :
        this(inPattern.toPositionMap(center), mapOf((0 to 0) to outCellType))
}

fun List<List<CellType>>.toPositionMap(center: Position): Map<Position, CellType> {
    return withIndex().flatMap { it ->
        val (y, row) = it
        row.mapIndexed { x, c ->
            (x - center.x to y - center.y) to c
        }
    }.toMap().filterValues { it -> it !is Any }
}