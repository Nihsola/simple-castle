package castle.core.screen

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen

class ScreenConfigurator(entitySystems: List<EntitySystem>) {
    val engine: PooledEngine = PooledEngine()
    val inputMultiplexer = InputMultiplexer()
    val screens = ArrayList<KtxScreen>()
    val disposables = ArrayList<Disposable>()

    init {
        entitySystems.forEach { withSystem(it) }
    }

    private fun withSystem(entitySystem: EntitySystem) {
        if (entitySystem is Disposable) {
            disposables.add(entitySystem)
        }
        if (entitySystem is KtxScreen) {
            screens.add(entitySystem)
        }
        if (entitySystem is KtxInputAdapter) {
            inputMultiplexer.addProcessor(entitySystem)
        }
        entitySystem.priority = Priority.order.indexOf(entitySystem.javaClass)
        engine.addSystem(entitySystem)
    }
}