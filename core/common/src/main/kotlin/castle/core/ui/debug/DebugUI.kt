package castle.core.ui.debug

import castle.core.component.render.StageRenderComponent
import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.service.CommonResources
import castle.core.service.GameService
import castle.core.service.MapService
import castle.core.service.UIService
import castle.core.system.PhysicSystem
import castle.core.system.UnitSystem
import castle.core.util.UIUtils
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport
import org.koin.core.annotation.Single

@Single
class DebugUI(
    spriteBatch: SpriteBatch,
    viewport: Viewport,
    private val commonResources: CommonResources,
    eventQueue: EventQueue
) : Entity() {
    private val stageRenderComponent: StageRenderComponent = StageRenderComponent(UIUtils.createStage(viewport, spriteBatch)).also { this.add(it) }
    private val signal = Signal<EventContext>()
    private val rootContainer = Container<Table>()
    private val rootTable = Table()

    private val fpsButton = createButton("FPS", listOf())

    var debugEnabled: Boolean = false
        set(value) {
            stageRenderComponent.stage.isDebugAll = value
            field = value
        }

    var isVisible: Boolean = false
        set(value) {
            rootContainer.isVisible = value
            field = value
        }

    init {
        signal.add(eventQueue)
        rootContainer.isVisible = false
        rootTable.add(main()).grow()
        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(10f)
        rootContainer.actor = rootTable
        stageRenderComponent.stage.addActor(rootContainer)
    }

    fun update() {
        val framesPerSecond = Gdx.graphics.framesPerSecond.toString()
        fpsButton.setText(framesPerSecond)
    }

    private fun main(): Container<out Group> {
        val table = Table()
        val container = Container<Table>().fill().align(Align.topRight)
        table.add(tabTopLeft()).grow()
        table.add(tabTopRight()).grow()
        container.actor = table
        return container
    }


    private fun tabTopLeft(): Container<Table> {
        val containerAbility = Container<Table>().align(Align.topLeft)
        val table = Table()
        table.add(fpsButton).width(50f).height(50f)
        containerAbility.actor = table
        return containerAbility
    }

    private fun tabTopRight(): Container<Table> {
        val container = Container<Table>().align(Align.topRight)
        val table = Table()
        table.add(createButton("Physic", listOf(EventContext(PhysicSystem.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        table.add(createButton("UI", listOf(EventContext(UIService.DEBUG_UI_ENABLE_1)))).width(50f).height(50f)
        table.row()
        table.add(createButton("DUI", listOf(EventContext(UIService.DEBUG_UI_ENABLE_2)))).width(50f).height(50f)
        table.row()
        table.add(createButton("Path", listOf(EventContext(UnitSystem.DEBUG_ENABLE)))).width(50f).height(50f)
        table.row()
        table.add(createButton("Grid", listOf(EventContext(MapService.DEBUG_GRID)))).width(50f).height(50f)
        table.row()
        val list = listOf(
            EventContext(GameService.DEBUG_SPAWN, mapOf(Pair(GameService.PLAYER_NAME, "Player 1"))),
            EventContext(GameService.DEBUG_SPAWN, mapOf(Pair(GameService.PLAYER_NAME, "Player 3")))
        )
        table.add(createButton("Spawn", list)).width(50f).height(50f)
        container.actor = table
        return container
    }

    private fun createButton(text: String, events: List<EventContext>): TextButton {
        val button = TextButton(text, commonResources.skin)
        button.color = Color.DARK_GRAY
        button.color.a = 0.5f
        button.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                events.forEach { signal.dispatch(it) }
            }
        })
        return button
    }
}