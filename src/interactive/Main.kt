package interactive

@JsName("init")
fun init(): Automaton {
    val automaton = Automaton(64, 48)

    val l1 = automaton.addLayer()

    l1.rules.add(WaterDownRule())
    l1.rules.add(WaterGenerateSpreadOnDirtRule())
    l1.rules.add(CustomPatternRule(
        mapOf(
            (0 to -1) to Water.Source,
            (0 to 0) to None
        ),
        mapOf(
            (0 to 0) to Water.Down
        )
    ))

    l1.rules.add(CustomPatternRule(
        listOf(
            listOf(Water.Down, None, Any),
            listOf(Water.Down, None, Any),
            listOf(Water.Down, None, Any)
        ),
        listOf(
            listOf(Any, Any, Grass),
            listOf(Any, Any, Any),
            listOf(Any, Grass, Any)
        ),
        1 to 1)
    )
    l1.cellTypes.add(Dirt)
    l1.cellTypes.add(None)
    l1.cellTypes.add(Water.Source)
    l1.cellTypes.add(Water.Spread)
    l1.cellTypes.add(Water.Down)
    l1.cellTypes.add(Water.Still)
    l1.cellTypes.add(Water.Bounce)
    l1.cellTypes.add(Grass)
    l1.set(1,1, Water.Source)

    return automaton
}