package be.anagon.cells

interface NamedRule : IRule {
    val name: String
}

interface EditableNamedRule : NamedRule {
    override var name: String
}