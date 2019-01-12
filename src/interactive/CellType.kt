package interactive

typealias Color = String

abstract class CellType {
    @JsName("getColor")
    abstract fun getColor(x: Int, y: Int): Color
}

object None : CellType() {
    override fun getColor(x: Int, y: Int): Color {
        return "#000000"
    }
}

object Void : CellType() {
    override fun getColor(x: Int, y: Int): Color {
        return "#FFFF00"
    }
}

object Any: CellType() {
    override fun getColor(x: Int, y: Int): Color {
        return "#9C0096"
    }
}

class CustomCellType(private val color: Color) : CellType() {
    override fun getColor(x: Int, y: Int): Color {
        return color
    }
}

object Dirt : CellType() {
    override fun getColor(x: Int, y: Int): Color = "#4d3d38"
}

object Grass : CellType() {
    override fun getColor(x: Int, y: Int): Color = "#3B4D28"

}

abstract class Water : CellType() {
    object Source : Water() {
        override fun getColor(x: Int, y: Int): Color = "#ffffff"
    }

    object Spread : Water() {
        override fun getColor(x: Int, y: Int): Color = "#31c3df"
    }

    object Down : Water() {
        override fun getColor(x: Int, y: Int): Color = "#81e6f1"
    }

    object Bounce : Water() {
        override fun getColor(x: Int, y: Int): Color = "#008ecc"
    }

    object Still : Water() {
        override fun getColor(x: Int, y: Int): Color = "#3a6fcf"
    }
}