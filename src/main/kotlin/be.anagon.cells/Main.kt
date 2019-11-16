package be.anagon.cells

import kotlin.random.Random

@JsName("init")
fun init(): App {
    val automaton = Automaton(64, 48)
//    val automaton = Automaton(128, 96)

    val above = pos(0, -1)
    val below = pos(0, 1)
    val center = pos(0, 0)

    val l1 = automaton.addLayer()



    l1.cellTypes.add(Dirt)
    l1.cellTypes.add(None)
    l1.cellTypes.add(Water.Source)
    l1.cellTypes.add(Water.Spread)
    l1.cellTypes.add(Water.Down)
    l1.cellTypes.add(Water.Still)
    l1.cellTypes.add(Water.Bounce)
    l1.cellTypes.add(Grass)
    l1.cellTypes.add(Gray)
    l1.cellTypes.add(Sand)
    l1.cellTypes.add(Alive)
    l1.cellTypes.add(Any)
    l1.cellTypes.add(ElectricityRule.Wire)
    l1.cellTypes.add(ElectricityRule.PoweredWire)
    l1.cellTypes.add(ElectricityRule.ClearWire)

    fun waterWorld() {
        l1.set(10, 3, Water.Source)

        for (x in 4..20) {
            l1.set(x, 20, Dirt)
        }
        l1.set(4, 19, Dirt)
        l1.set(20, 19, Dirt)
    }

    //addWaterRules(l1, center, below, above)
    //waterWorld()

    return App(automaton)
}

private fun addWaterRules(
    l1: Layer,
    center: Position,
    below: Position,
    above: Position
) {
    l1.rules.add(WaterOverflowRule())
    l1.rules.add(WaterRule())

//     generate spread on dirt
    l1.rules.add(
        EditablePatternRule(
            mapOf(center to Water.Down, below to Dirt),
            mapOf(center to Water.Spread)
        )
    )

    // generate down
    l1.rules.add(
        EditablePatternRule(
            mapOf(above to Water.Source, center to None),
            mapOf(center to Water.Down)
        )
    )

    // propagate down
    l1.rules.add(
        EditablePatternRule(
            mapOf(above to Water.Down, center to None),
            mapOf(center to Water.Down)
        )
    )

//     generate spread
    l1.rules.add(
        EditablePatternRule(
            mapOf(center to Water.Down, below to Water.Still),
            mapOf(center to Water.Spread)
        )
    )
}
