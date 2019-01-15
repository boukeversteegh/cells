package interactive

import kotlin.js.*

class JsonMapper {
    @JsName("mapLayer")
    fun map(layer: Layer): Json {
        return json(
            "cellTypes" to map(layer.cellTypes),
            "rules" to layer
                .rules
                .filter { it is CustomPatternRule }
                .map {
                    it as CustomPatternRule
                }
                .map {
                    json(
                        "input" to map(it.input, layer.cellTypes),
                        "output" to map(it.output, layer.cellTypes)
                    )
                }
                .toTypedArray()
        )
    }

    @JsName("loadLayer")
    fun map(layer: Layer, layerState: Json) {
        val cellTypesState = layerState["cellTypes"].unsafeCast<Array<Json>>()

        layer.cellTypes.clear()
        for (cellTypeState in cellTypesState) {
            val cellType = when (cellTypeState["type"]) {
                "any" -> Any
                else -> CustomCellType(cellTypeState["color"].toString())
            }
            layer.cellTypes.add(cellType)
        }

        val rulesState = layerState["rules"].unsafeCast<Array<Json>>()

        layer.rules.clear()

        fun mapPattern(patternState: Array<Json>): Map<Position, CellType> {
            return patternState.map {
                (it["position"].unsafeCast<Json>()["x"].unsafeCast<Int>() to it["position"].unsafeCast<Json>()["y"].unsafeCast<Int>()) to layer.cellTypes[it["cellType"].unsafeCast<Int>()]
            }.toMap()
        }

        for (ruleState in rulesState) {
            val inputState: Array<Json> = ruleState["input"].unsafeCast<Array<Json>>()
            val outputState = ruleState["output"].unsafeCast<Array<Json>>()

            val input = mapPattern(inputState)
            val output = mapPattern(outputState)

            layer.rules.add(CustomPatternRule(input, output))
        }
    }

    private fun map(cellTypes: List<CellType>): Array<Json> {
        return cellTypes.map {
            map(it)
        }.toTypedArray()
    }

    private fun map(cellType: CellType): Json {
        return when (cellType) {
            is CustomCellType -> json("color" to cellType.color, "type" to "custom")
            is Any -> json("type" to "any")
            else -> json("color" to cellType.getColor(0, 0), "type" to "default")
        }
    }

    fun map(positions: Map<Position, CellType>, cellTypes: List<CellType>): Array<Json> {
        return positions.map {
            json(
                "position" to map(it.key),
                "cellType" to map(it.value, cellTypes)
            )
        }.toTypedArray()
    }

    private fun map(cellType: CellType, cellTypes: List<CellType>): Int {
        console.log(cellType, cellTypes, cellTypes.indexOf(cellType))
        return cellTypes.indexOf(cellType)
    }

    fun map(position: Position): Json {
        return json(
            "x" to position.x,
            "y" to position.y
        )
    }

    fun map(rule: Rule) {
//        JSON.stringify(rule.)
    }
}