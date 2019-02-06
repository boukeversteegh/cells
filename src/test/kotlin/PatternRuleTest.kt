import be.anagon.cells.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PatternRuleTest {
    object EmptyCell : CellType() {
        override fun getColor(x: Int, y: Int): Color {
            return "#000000"
        }
    }

    object FilledCell : CellType() {
        override fun getColor(x: Int, y: Int): Color {
            return "#000000"
        }
    }

    private val rule = CustomPatternRule(
        mapOf(
            Position(0, 0) to FilledCell,
            Position(0, 1) to EmptyCell
        ), mapOf(
            Position(0, 1) to FilledCell
        )
    )

    @Test
    fun evaluate() {
        val output = rule.evaluate(
            Position(10, 10),
            mapOf(
                Position(10, 10) to FilledCell,
                Position(10, 11) to EmptyCell
            )
        )

        assertEquals(mapOf(Position(10, 11) to FilledCell), output)
    }

    @Test
    fun rotatable_rule_applies_in_rotated_variation() {
        rule.rotatable = true

        val output = rule.evaluate(
            Position(10, 10),
            mapOf(
                Position(10, 10) to FilledCell,
                Position(11, 10) to EmptyCell
            )
        )

        assertEquals(mapOf(Position(11, 10) to FilledCell), output)
    }

    @Test
    fun rotatable_rule_applies_in_all_directions() {
        rule.rotatable = true

        val output = rule.evaluate(
            pos(10, 10),
            mapOf(
                pos(10, 9) to EmptyCell,
                pos(9, 10) to EmptyCell, pos(10, 10) to FilledCell, pos(11, 10) to EmptyCell,
                pos(10, 11) to EmptyCell
            )
        )

        assertEquals(mapOf(
            pos(10, 9) to FilledCell,
            pos(9, 10) to FilledCell, /* original cell here */ pos(11, 10) to FilledCell,
            pos(10, 11) to FilledCell
        ), output)
    }
}