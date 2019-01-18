package interactive

typealias MutablePatternMap = MutableMap<Position, CellType>

class CustomPatternRule(override val input: MutablePatternMap, override val output: MutablePatternMap) : PatternRule() {

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