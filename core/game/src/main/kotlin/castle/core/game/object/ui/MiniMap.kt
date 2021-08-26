package castle.core.game.`object`.ui

import castle.core.common.component.Rect2DComponent
import castle.core.game.`object`.unit.GameObject
import castle.core.game.path.Area
import castle.core.game.service.ScanService
import castle.core.game.GameContext
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class MiniMap(
    private val gameContext: GameContext,
    screenWidth: Int, screenHeight: Int,
    scanService: ScanService
) : Actor() {
    private val miniMapWidth: Float = screenWidth * 0.2f
    private val miniMapHeight: Float = screenHeight * 0.3f
    private val startPositionX = screenWidth - miniMapWidth - 10f
    private val startPositionY = 10f

    private val miniMap: MutableList<MutableList<MinimapPiece>> = initializeMinimap(scanService.map)
    private val miniMapBuffer: MutableList<MinimapPiece> = ArrayList()

    init {
//        for (peace in miniMap) {
//            width += peace[0].width()
//        }
//
//        height = width
//
//        x = 100f
//        y = 30f
    }

    fun update(objectsOnMap: Map<Area, GameObject>) {
        miniMapBuffer.forEach { it.reset() }
        miniMapBuffer.clear()
        objectsOnMap.forEach {
            val area = it.key
            miniMapBuffer.add(miniMap[area.y][area.x])
            miniMap[area.y][area.x].setColor(Color.RED)
        }
    }

    private fun initializeMinimap(miniMap: List<List<Int>>): MutableList<MutableList<MinimapPiece>> {
        val pointWidth = miniMapWidth / miniMap.size
        val pointHeight = miniMapHeight / miniMap[0].size

        val pieces: MutableList<MutableList<MinimapPiece>> = ArrayList()
        for (i in miniMap.indices) {
            pieces.add(ArrayList())
            for (j in miniMap[i].indices) {
                pieces[i].add(
                    MinimapPiece(
                        gameContext.engine,
                        pointWidth, pointHeight,
                        Vector2(i.toFloat(), j.toFloat()),
                        Vector2(startPositionX, startPositionY), miniMap[i][j]
                    )
                )
            }
        }
        return pieces
    }

    private class MinimapPiece(
        engine: Engine,
        width: Float, height: Float,
        paramIndex: Vector2, offsetParam: Vector2,
        private val groundHeight: Int
    ) {
        private val entity = engine.createEntity().apply { engine.addEntity(this) }
        private val rect2DComponent: Rect2DComponent = Rect2DComponent().apply { entity.add(this) }

        init {
            rect2DComponent.height = height
            rect2DComponent.width = width
            rect2DComponent.position.set(
                offsetParam.x + paramIndex.x * rect2DComponent.width,
                offsetParam.y + paramIndex.y * rect2DComponent.height
            )
            rect2DComponent.color = getGroundColor(groundHeight)
        }

        fun setColor(color: Color) = color.also { rect2DComponent.color = it }

        fun reset() = getGroundColor(groundHeight).also { rect2DComponent.color = it }

        fun width() = rect2DComponent.width

        private fun getGroundColor(value: Int): Color {
            val baseHsv = FloatArray(3)
            Color.valueOf("#00ff4c").toHsv(baseHsv)
            baseHsv[2] = baseHsv[2] - 0.2f * value
            return Color().fromHsv(baseHsv)
        }
    }
}