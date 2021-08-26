package castle.core.game.`object`.ui

import castle.core.common.component.StageComponent
import castle.core.common.config.GUIConfig
import castle.core.game.GameContext
import castle.core.game.event.EventContext
import castle.core.game.event.EventQueue
import castle.core.game.service.ScanService
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Value
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Disposable

class GameUI(
    private val gameContext: GameContext,
    scanService: ScanService,
    guiConfig: GUIConfig,
    private val eventQueue: EventQueue
) : Disposable {
    private val entity = gameContext.engine.createEntity().apply { gameContext.engine.addEntity(this) }
    private val stageComponent: StageComponent = StageComponent(guiConfig.stage).apply { entity.add(this) }

    private val signal = Signal<EventContext>().apply { this.add(eventQueue) }

    private val rootContainer = Container<Group>()
    private val chatUI = Container<Group>()

    val miniMap: MiniMap = MiniMap(gameContext, Gdx.graphics.width, Gdx.graphics.height, scanService)
    val chat = Chat(signal, gameContext.resourceService)

    var debugEnabled: Boolean = true
        set(value) {
            stageComponent.stage.isDebugAll = value
            field = value
        }

    init {
        debugEnabled = true

        rootContainer.setFillParent(true)
        rootContainer.fill()
        rootContainer.pad(20f)

        chatUI
            .left()
            .bottom()
            .width(Value.percentWidth(0.25f, rootContainer))
            .height(Value.percentHeight(0.25f, rootContainer))

        chatUI.actor = chat
        rootContainer.actor = chatUI

        stageComponent.stage.addActor(rootContainer)

        stageComponent.stage.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (event.target is Group) {
                    stageComponent.stage.unfocusAll()
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
    }
}