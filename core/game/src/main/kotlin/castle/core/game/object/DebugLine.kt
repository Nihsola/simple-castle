package castle.core.game.`object`

import castle.core.game.GameContext
import castle.core.common.component.Line3DComponent
import castle.core.common.util.GlobalContext
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class DebugLine(private val gameContext: GameContext) : Disposable {
    private val entity: Entity = gameContext.engine.createEntity().apply { gameContext.engine.addEntity(this) }
    private val line3DComponent: Line3DComponent = gameContext.engine.createComponent(Line3DComponent::class.java).apply { entity.add(this) }

    var from: Vector3
        get() {
            return line3DComponent.from
        }
        set(value) {
            line3DComponent.from.set(value)
        }

    var to: Vector3
        get() {
            return line3DComponent.to
        }
        set(value) {
            line3DComponent.to.set(value)
        }

    var color: Color
        get() {
            return line3DComponent.color
        }
        set(value) {
            line3DComponent.color.set(value)
        }

    var show: Boolean
        get() {
            return line3DComponent.show
        }
        set(value) {
            line3DComponent.show = value && GlobalContext.isDebugEnabled
        }

    override fun dispose() {
        gameContext.engine.removeEntity(entity)
    }
}