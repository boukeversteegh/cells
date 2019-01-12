package interactive

class CustomPatternRule(override val input: MutableMap<Position, CellType>, override val output: MutableMap<Position, CellType>) : PatternRule() {
    constructor(
        inPattern: List<List<CellType>>,
        outPattern: List<List<CellType>>,
        center: Position = 1 to 1
    ) :
        this(inPattern.toPositionMap(center).toMutableMap(), outPattern.toPositionMap(center).toMutableMap())

    constructor(
        inPattern: List<List<CellType>>,
        outCellType: CellType,
        center: Position
    ) :
        this(inPattern.toPositionMap(center).toMutableMap(), mapOf((0 to 0) to outCellType).toMutableMap())

    constructor(input: Map<Position, CellType>, output: Map<Position, CellType>) : this(input.toMutableMap(), output.toMutableMap())

    @JsName("setInput")
    fun setInput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            input.remove(x to y)
        } else {
            input[x to y] = c
        }
    }

    @JsName("setOutput")
    fun setOutput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            output.remove(x to y)
        } else {
            output[x to y] = c
        }
    }
}

fun List<List<CellType>>.toPositionMap(center: Position): Map<Position, CellType> {
    return withIndex().flatMap { it ->
        val (y, row) = it
        row.mapIndexed { x, c ->
            (x - center.x to y - center.y) to c
        }
    }.toMap().filterValues { it -> it !is Any }
}