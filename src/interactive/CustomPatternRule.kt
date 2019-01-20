package interactive

import kotlin.js.Json
import kotlin.js.json

typealias MutablePatternMap = MutableMap<Position, CellType>

class CustomPatternRule(
    override val input: MutablePatternMap,
    override val output: MutablePatternMap
) : PatternRule(), SerializableRule {

    override fun serialize(cellTypes: List<CellType>): Json {
        return json(
            "type" to "CustomPatternRule",
            "input" to toJson(input, cellTypes),
            "output" to toJson(output, cellTypes)
        )
    }

    override val key = CustomPatternRule.key

    companion object {
        const val key = "CustomPatternRule"

        fun toJson(positions: Map<Position, CellType>, cellTypes: List<CellType>): Array<Json> {
            return positions.map {
                json(
                    "position" to it.key.serialize(),
                    "cellType" to it.value.serialize(cellTypes)
                )
            }.toTypedArray()
        }

        fun deserialize(ruleState: Json, cellTypes: List<CellType>): CustomPatternRule {
            fun mapPattern(patternState: Array<Json>): Map<Position, CellType> {
                return patternState.map {
                    val x = it["position"].unsafeCast<Json>()["x"].unsafeCast<Int>()
                    val y = it["position"].unsafeCast<Json>()["y"].unsafeCast<Int>()
                    pos(x, y) to cellTypes[it["cellType"].unsafeCast<Int>()]
                }.toMap()
            }

            val inputState = ruleState["input"].unsafeCast<Array<Json>>()
            val outputState = ruleState["output"].unsafeCast<Array<Json>>()

            val input = mapPattern(inputState)
            val output = mapPattern(outputState)

            return CustomPatternRule(input, output)
        }
    }

    constructor(input: Map<Position, CellType>, output: Map<Position, CellType>) : this(input.toMutableMap(), output.toMutableMap())

    @JsName("setInput")
    fun setInput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            input.remove(pos(x, y))
        } else {
            input[pos(x, y)] = c
        }
    }

    @JsName("setOutput")
    fun setOutput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            output.remove(pos(x, y))
        } else {
            output[pos(x, y)] = c
        }
    }

    @JsName("getPositions")
    fun getPositions(): Set<Position> {
        return input.keys + output.keys
    }

    @JsName("getMinPosition")
    fun getMinPosition(): Position {
        // todo how to sort by (y, x)?
        val minX = getPositions().map { it.x }.min() ?: 0
        val minY = getPositions().map { it.y }.min() ?: 0
        return pos(minX, minY)
    }

    @JsName("getMaxPosition")
    fun getMaxPosition(): Position {
        // todo how to sort by (y, x)?
        val maxX = getPositions().map { it.x }.max() ?: 0
        val maxY = getPositions().map { it.y }.max() ?: 0
        return pos(maxX, maxY)
    }

    fun getArea(): Array<Array<Position>> {
        val min = getMinPosition()
        val max = getMaxPosition()
        return Array(
            max.y - min.y
        ) { y ->
            Array(max.x - min.x) { x ->
                pos(x, y)
            }
        }
    }

}