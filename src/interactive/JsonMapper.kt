package interactive

import kotlin.js.*

class JsonMapper {
    @JsName("mapLayer")
    fun toJson(layer: Layer): Json {
        return json(
            "cellTypes" to toJson(layer.cellTypes),
            "rules" to layer
                .rules
                .filter { it is CustomPatternRule }
                .map {
                    it as CustomPatternRule
                }
                .map {
                    json(
                        "input" to toJson(it.input, layer.cellTypes),
                        "output" to toJson(it.output, layer.cellTypes)
                    )
                }
                .toTypedArray()
        )
    }

    @JsName("loadLayer")
    fun fromJson(layer: Layer, layerState: Json) {
        val cellTypesState = layerState["cellTypes"].unsafeCast<Array<Json>>()

        layer.cellTypes.clear()
        for (cellTypeState in cellTypesState) {
            val cellType = when (cellTypeState["type"]) {
                "Any", "any" -> Any
                "None" -> None
                "Alive" -> Alive
                "Void" -> Void
                else -> CustomCellType(cellTypeState["color"].toString())
            }
            layer.cellTypes.add(cellType)
        }

        val rulesState = layerState["rules"].unsafeCast<Array<Json>>()

        fun mapPattern(patternState: Array<Json>): Map<Position, CellType> {
            return patternState.map {
                val x = it["position"].unsafeCast<Json>()["x"].unsafeCast<Int>()
                val y = it["position"].unsafeCast<Json>()["y"].unsafeCast<Int>()
                pos(x, y) to layer.cellTypes[it["cellType"].unsafeCast<Int>()]
            }.toMap()
        }

        layer.clear()
        layer.rules.clear()
        for (ruleState in rulesState) {
            val inputState = ruleState["input"].unsafeCast<Array<Json>>()
            val outputState = ruleState["output"].unsafeCast<Array<Json>>()

            val input = mapPattern(inputState)
            val output = mapPattern(outputState)

            layer.rules.add(CustomPatternRule(input, output))
        }
        console.log(layer.rules)
    }

    private fun toJson(cellTypes: List<CellType>): Array<Json> {
        return cellTypes.map {
            toJson(it)
        }.toTypedArray()
    }

    private fun toJson(cellType: CellType): Json {
        return when (cellType) {
            is CustomCellType -> json("color" to cellType.color, "type" to "custom")
            is None -> json("type" to cellType::class.simpleName)
            else -> json("color" to cellType.getColor(0, 0), "type" to "default")
        }
    }

    fun toJson(positions: Map<Position, CellType>, cellTypes: List<CellType>): Array<Json> {
        return positions.map {
            json(
                "position" to toJson(it.key),
                "cellType" to toJson(it.value, cellTypes)
            )
        }.toTypedArray()
    }

    private fun toJson(cellType: CellType, cellTypes: List<CellType>): Int {
        console.log(cellType, cellTypes, cellTypes.indexOf(cellType))
        return cellTypes.indexOf(cellType)
    }

    fun toJson(position: Position): Json {
        return json(
            "x" to position.x,
            "y" to position.y
        )
    }
}