package be.anagon.cells

class App(val automaton: Automaton) {
    val Rules = RulesController()

    inner class RulesController {
        val changeListeners = mutableListOf<(Array<Rule>) -> Unit>()
        val selectListeners = mutableListOf<(Rule?) -> Unit>()
        val updateListeners = mutableListOf<(Rule) -> Unit>()
        var selected: Rule? = null

        val types = listOf(
            EditablePatternRule,
            RandomWalkRule,
            ElectricityRule
        ).toTypedArray()

        val typesByKey = mapOf<String, () -> Rule>(
            EditablePatternRule.key to { EditablePatternRule.new() },
            RandomWalkRule.key to { RandomWalkRule.new() },
            ElectricityRule.key to { ElectricityRule() }
        )

        private val rules = automaton.layers[0].rules

        @JsName("getTypes")
        fun getTypes() = types

        @JsName("addRule")
        fun addRule(rule: Rule) {
            automaton.layers[0].addRule(rule)
            doChange()
        }

        @JsName("addByKey")
        fun addByKey(key: String) {
            val factory = typesByKey[key]

            if (factory != null) {
                addRule(factory.invoke())
            }
        }

        @JsName("newPatternRule")
        fun newPatternRule(): EditablePatternRule {
            return EditablePatternRule.new()
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
        fun doUpdate(rule: Rule) {
            updateListeners.forEach { it(rule) }
        }

        @JsName("onSelect")
        fun onSelect(listener: (Rule?) -> Unit) {
            selectListeners += listener
            listener(selected)
        }

        private fun doSelect() {
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

        private fun doChange() {
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

        private fun doSelect() {
            onSelectListeners.forEach { it(selected) }
        }
    }

    val EditablePatternRules = object {
        @JsName("setInput")
        fun setInput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setInput(position.x, position.y, cellType)
            Rules.doUpdate(rule)
        }

        @JsName("setOutput")
        fun setOutput(rule: EditablePatternRule, position: Position, cellType: CellType) {
            rule.setOutput(position.x, position.y, cellType)
            Rules.doUpdate(rule)
        }
    }
}