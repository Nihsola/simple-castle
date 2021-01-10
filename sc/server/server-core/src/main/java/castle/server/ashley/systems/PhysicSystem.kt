package castle.server.ashley.systems

import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.service.PhysicService
import castle.server.ashley.systems.adapter.IteratingSystemAdapter
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Input

class PhysicSystem(private val physicService: PhysicService) : EntityListener,
        IteratingSystemAdapter(Family.all(PositionComponent::class.java, PhysicComponent::class.java).get()) {

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(Family.all(PositionComponent::class.java, PhysicComponent::class.java).get(), this)
        super.addedToEngine(engine)
    }

    override fun entityRemoved(entity: Entity) {
        physicService.removeEntity(entity)
    }

    override fun entityAdded(entity: Entity) {
        physicService.addEntity(entity)
    }

    override fun render(delta: Float) {
        physicService.renderDebug()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.F1) {
            physicService.isDebug = !physicService.isDebug
        }
        return super.keyDown(keycode)
    }

    override fun update(deltaTime: Float) {
        physicService.update(deltaTime)
    }

    override fun dispose() {
        physicService.dispose(entities)
    }
}