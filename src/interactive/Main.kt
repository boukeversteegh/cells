package interactive

@JsName("init")
fun init(): Automaton {
    val automaton = Automaton(64, 48)

    val l1 = automaton.addLayer()

    l1.rules.add(WaterDownRule())
    l1.cellTypes.add(Dirt)
    l1.cellTypes.add(None)
    l1.cellTypes.add(Water.Source)
    l1.cellTypes.add(Water.Spread)
    l1.cellTypes.add(Water.Down)
    l1.cellTypes.add(Water.Still)
    l1.cellTypes.add(Water.Bounce)
    l1.set(1,1, Water.Source)

    return automaton
}