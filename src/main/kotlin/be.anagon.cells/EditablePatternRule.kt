package be.anagon.cells

import kotlin.js.Json
import kotlin.js.json

typealias MutablePatternMap = MutableMap<Position, CellType>

class EditablePatternRule(
    override val input: MutablePatternMap,
    override val output: MutablePatternMap
) : PatternRule(), SerializableRule, NamedRule {

    override var rotatable = false
        set(value) {
            field = value
            rotatedInputs = getRotatedPatternMaps(input)
            rotatedOutputs = getRotatedPatternMaps(output)
        }

    override val key = Companion.key

    override var name = "Pattern Rule"

    override fun isEditable() = true

    override var rotatedInputs: Array<PatternMap> = getRotatedPatternMaps(input)
    override var rotatedOutputs: Array<PatternMap> = getRotatedPatternMaps(output)

    private fun getRotatedPatternMaps(patternMap: MutablePatternMap): Array<PatternMap> {
        return if (rotatable) arrayOf(
            patternMap.rotateRight(),
            patternMap.rotateRight().rotateRight(),
            patternMap.rotateRight().rotateRight().rotateRight()
        ) else emptyArray()
    }

    companion object {
        const val key = "CustomPatternRule"

        @JsName("new")
        fun new() = EditablePatternRule(emptyMap(), emptyMap())

        fun toJson(positions: Map<Position, CellType>, cellTypes: List<CellType>): Array<Json> {
            return positions.map {
                json(
                    "position" to it.key.serialize(),
                    "cellType" to it.value.serialize(cellTypes)
                )
            }.toTypedArray()
        }

        fun deserialize(ruleState: Json, cellTypes: List<CellType>): EditablePatternRule {
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

            val rule = EditablePatternRule(input, output)

            val name = ruleState["name"].unsafeCast<String?>()
            if (name != null) {
                rule.name = name
            }

            rule.rotatable = ruleState["rotatable"].unsafeCast<Boolean>()

            return rule
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
        rotatedInputs = getRotatedPatternMaps(input)
    }

    @JsName("setOutput")
    fun setOutput(x: Int, y: Int, c: CellType) {
        if (c is Any) {
            output.remove(pos(x, y))
        } else {
            output[pos(x, y)] = c
        }
        rotatedOutputs = getRotatedPatternMaps(output)
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

    @Deprecated("not used for now")
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

    override fun serialize(cellTypes: List<CellType>): Json {
        return json(
            "type" to "CustomPatternRule",
            "input" to toJson(input, cellTypes),
            "output" to toJson(output, cellTypes),
            "name" to name,
            "rotatable" to rotatable
        )
    }
}