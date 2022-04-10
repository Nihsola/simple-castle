package castle.core.json.settings

data class UnitSettings(
        val enabled: Boolean = true,
        val unitType: String = "building",
        val behaviour: String = "none",
        val amount: Int = 10,
        val speedLinear: Float = 0.0f,
        val speedAngular: Float = 0.0f,
        val attackFrom: Int = 0,
        val attackTo: Int = 0,
        val attackSpeed: Float = 0f,
        val scanRange: Float = 0f
)