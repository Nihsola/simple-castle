package castle.core.system

import castle.core.event.EventContext
import castle.core.event.EventQueue
import castle.core.service.CameraService
import castle.core.service.EnvironmentInitService
import castle.core.service.PlayerService
import castle.core.service.SelectionService
import castle.core.ui.service.UIService
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.signals.Signal
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class GameManagerSystem(
        private val environmentInitService: EnvironmentInitService,
        private val playerService: PlayerService,
        private val uiService: UIService,
        private val selectionService: SelectionService,
        private val cameraService: CameraService,
        private val eventQueue: EventQueue
) : IntervalSystem(GAME_TICK), KtxInputAdapter, KtxScreen {
    companion object {
        private const val GAME_TICK: Float = 0.1f
        const val CHAT_FOCUSED = "CHAT_FOCUSED"
        const val CHAT_UNFOCUSED = "CHAT_UNFOCUSED"
    }

    private val signal = Signal<EventContext>()

    init {
        signal.add(eventQueue)
    }

    override fun addedToEngine(engine: Engine) {
        environmentInitService.init(engine)
        uiService.init(engine)
        selectionService.init(engine)
        playerService.init(engine)
    }

    override fun update(deltaTime: Float) {
        cameraService.update(deltaTime)
        super.update(deltaTime)
    }

    override fun updateInterval() {
        uiService.update()
        playerService.update(engine, eventQueue)
        proceedEvents()
    }

    private fun proceedEvents() {
        eventQueue.proceed {
            when (it.eventType) {
                CHAT_FOCUSED -> {
                    cameraService.input = false
                    true
                }
                CHAT_UNFOCUSED -> {
                    cameraService.input = true
                    true
                }
                else -> false
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        cameraService.resize(width, height)
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.ESCAPE -> {
                Gdx.app.exit()
            }
        }
        return cameraService.keyDown(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return cameraService.keyTyped(character)
    }

    override fun keyUp(keycode: Int): Boolean {
        return cameraService.keyUp(keycode)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return cameraService.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return cameraService.scrolled(amountX, amountY)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        cameraService.touchDown(screenX, screenY, pointer, button)
        selectionService.select(screenX, screenY, engine.entities)
        return uiService.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return cameraService.touchDragged(screenX, screenY, pointer)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return cameraService.touchUp(screenX, screenY, pointer, button)
    }
}