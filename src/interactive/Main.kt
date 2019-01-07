package interactive

@JsName("init")
fun init(): Automaton {
    val automaton = Automaton(64, 48)

    val l1 = automaton.addLayer()
//    val c0 = CustomCellType("#4d3d38")

//    l1.cellTypes.add(c0)
//    l1.set(1, 1, c0)

    l1.set(1,1, Water.Source)

    return automaton
}