package be.anagon.cells

class App(val automaton: Automaton) {
    val Rules = object {
        val changeListeners = mutableListOf<(Array<Rule>) -> Unit>()
        val selectListeners = mutableListOf<(Rule?) -> Unit>()
        val updateListeners = mutableListOf<(Rule) -> Unit>()
        var selected: Rule? = null

        val types = listOf(
            CustomPatternRule,
            RandomWalkRule
        ).toTypedArray()

        private val rules = automaton.layers[0].rules

        @JsName("getTypes")
        fun getTypes() = types

        @JsName("addRule")
        fun addRule(rule: Rule) {
            automaton.layers[0].addRule(rule)
            doChange()
        }

        @JsName("onChange")
        fun onChange(listener: (Array<Rule>) -> Unit) {
            changeListeners += listener
            listener(rules.toTypedArray())
        }

        @JsName("onUpdate")
        fun onUpdate(listener: (Rule) -> Unit) {
            updateListeners += listener
        }

        @JsName("doUpdate")
        @Deprecated("only allow changing rules through App class", ReplaceWith("App.Rules.___"))
        fun doUpdate(rule: Rule) {
            updateListeners.forEach { it(rule) }
        }

        @JsName("onSelect")
        fun onSelect(listener: (Rule?) -> Unit) {
            selectListeners += listener
            listener(selected)
        }

        fun doSelect() {
            selectListeners.forEach { it(selected) }
        }

        @JsName("select")
        fun select(rule: Rule?) {
            selected = rule
            doSelect()
        }


        @JsName("clear")
        fun clear() {
            rules.clear()
            doChange()
        }

        @JsName("delete")
        fun delete(rule: Rule) {
            val index = rules.indexOf(rule)
            rules.remove(rule)
            select(rules.getOrNull(index))
            doChange()
        }

        fun doChange() {
            val rules = rules.toTypedArray()
            changeListeners.forEach { it(rules) }
        }
    }

    val CellTypes = object {
        var selected: CellType = Dirt

        val onSelectListeners = mutableListOf<(CellType) -> Unit>()

        @JsName("select")
        fun select(cellType: CellType) {
            selected = cellType
            doSelect()
        }

        @JsName("onSelect")
        fun onSelect(listener: (CellType) -> Unit) {
            onSelectListeners += listener
            listener(selected)
        }

        fun doSelect() {
            onSelectListeners.forEach { it(selected) }
        }
    }
}