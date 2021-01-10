package castle.server.ashley.physic

import castle.server.ashley.component.AnimationComponent
import castle.server.ashley.component.PhysicComponent
import castle.server.ashley.component.PositionComponent
import castle.server.ashley.component.RenderComponent
import castle.server.ashley.utils.json.SceneObjectJson
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.utils.Array

class Constructor(private val model: Model, sceneObjectJson: SceneObjectJson) {
    private var collisionFlag: String = sceneObjectJson.collisionFlag
    private val physicShape: PhysicShape = sceneObjectJson.shape
    private val armature: String = sceneObjectJson.armature
    private var collisionFilterMask: List<Int> = sceneObjectJson.collisionFilterMask

    val node: String = sceneObjectJson.nodes
    var collisionFilterGroup: Int = sceneObjectJson.collisionFilterGroup
    val mass: Float = sceneObjectJson.mass
    val instantiate: Boolean = sceneObjectJson.instantiate
    val hide: Boolean = sceneObjectJson.hide
    var animation: String = sceneObjectJson.animation

    fun getPhysicObject(): PhysicObject {
        val shape = physicShape.build(getModel())
        val info: btRigidBody.btRigidBodyConstructionInfo = btRigidBody.btRigidBodyConstructionInfo(mass, null, shape)
        return PhysicObject(info, collisionFlag, collisionFilterGroup, collisionFilterMask)
    }

    fun getRenderModel(): ModelInstance {
        return getModel().apply {
            transform.mul(model.getNode(node).localTransform)
            nodes.forEach { node ->
                node.translation.set(0f, 0f, 0f)
                node.scale.set(1f, 1f, 1f)
                calculateTransforms()
            }
        }
    }

    fun getTransform(): Matrix4 {
        return model.getNode(node).globalTransform.cpy()
    }

    private fun getModel(): ModelInstance {
        val array = if (armature.isNotEmpty()) listOf(armature, node).toTypedArray() else listOf(node).toTypedArray()
        return ModelInstance(model, Array(array))
    }

    fun instantiate(engine: Engine): Entity {
        val entity: Entity = engine.createEntity()

        val positionComponent = PositionComponent.createComponent(engine, this)
        entity.add(positionComponent)

        if (!this.hide) {
            val renderComponent = RenderComponent.createComponent(engine, this)
            entity.add(renderComponent)
        }

        if (this.animation.isNotEmpty()) {
            val animationComponent = AnimationComponent.createComponent(engine, this)
            entity.add(animationComponent)
        }

        val physicComponent = PhysicComponent.createComponent(engine, this)
        entity.add(physicComponent)

        return entity
    }
}