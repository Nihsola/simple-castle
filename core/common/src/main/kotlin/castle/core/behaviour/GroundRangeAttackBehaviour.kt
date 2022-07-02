package castle.core.behaviour

import castle.core.behaviour.controller.GroundRangeUnitController
import castle.core.component.MapComponent
import castle.core.component.StateComponent
import castle.core.component.UnitComponent
import castle.core.state.StateDelta
import com.badlogic.ashley.core.Entity
import org.koin.core.annotation.Single

@Single
class GroundRangeAttackBehaviour(private val controller: GroundRangeUnitController) {
    private val init = Init()
    private val main = Main()

    fun initState(): StateDelta<Entity> = init

    private inner class Init : StateDelta<Entity> {
        override fun update(entity: Entity) {
            StateComponent.mapper.get(entity).state.changeState(main)
        }
    }

    private inner class Main : StateDelta<Entity> {
        override fun enter(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = true
        }

        override fun update(entity: Entity) {
            val unitComponent = UnitComponent.mapper.get(entity)
            val mapComponent = MapComponent.mapper.get(entity)
            if (mapComponent.isUnitsAround) {
//                val enemies = UnitUtils.findEnemies(unitComponent, mapComponent.inRadiusUnits)
//                if (enemies.isNotEmpty()) {
//                    controller.spawnProjectile(unitComponent)
//                }
            }
        }

        override fun exit(entity: Entity) {
            MapComponent.mapper.get(entity).shouldSearchEntities = false
        }
    }
}