package interactive

import kotlin.js.Json
import kotlin.js.json

class JsonMapper {
    @JsName("mapLayer")
    fun toJson(layer: Layer): Json = layer.serialize()

    @JsName("loadLayer")
    fun fromJson(layer: Layer, layerState: Json) {
        val cellTypesState = layerState["cellTypes"].unsafeCast<Array<Json>>()

        layer.cellTypes.clear()
        for (cellTypeState in cellTypesState) {
            val cellType = when (cellTypeState["type"]) {
                "Any", "any" -> Any
                "Alive" -> Alive
                "Void" -> Void
                "Source" -> Water.Source
                "None" -> None
                "Spread" -> Water.Spread
                "Down" -> Water.Down
                "Still" -> Water.Still
                "Bounce" -> Water.Bounce
                "Grass" -> Grass
                "Gray" -> Gray
                "Sand" -> Sand
                "Wire" -> ElectricityRule.Wire
                "PoweredWire" -> ElectricityRule.PoweredWire

                else -> CustomCellType(cellTypeState["color"].toString())
            }
            layer.cellTypes.add(cellType)
        }

        val rulesState: Array<Json> = layerState["rules"].unsafeCast<Array<Json>>()


        layer.clear()
        layer.rules.clear()

        val ruleTypes = mapOf<String, (Json) -> Rule>(
            CustomPatternRule.key to { it -> CustomPatternRule.deserialize(it, layer.cellTypes) },
            ElectricityRule.key to { _ -> ElectricityRule.deserialize() }
        )

        // Load Rules
        for (ruleState in rulesState) {
            val ruleType = ruleState["type"]
            val ruleDeserializer = ruleTypes[ruleType]

            if (ruleDeserializer != null) {
                val rule = ruleDeserializer.invoke(ruleState)
                layer.rules.add(rule)
            }
        }
    }
}