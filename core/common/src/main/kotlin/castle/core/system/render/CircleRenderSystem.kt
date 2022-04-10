package castle.core.system.render

import castle.core.component.render.CircleRenderComponent
import castle.core.service.CameraService
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class CircleRenderSystem(
        private val shapeRenderer: ShapeRenderer,
        private val cameraService: CameraService
) : IteratingSystem(Family.all(CircleRenderComponent::class.java).get()) {
    private val tempVector3 = Vector3()
    private val tempMatrix4 = Matrix4()

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = cameraService.currentCamera.camera.combined
        shapeRenderer.begin()
        super.update(deltaTime)
        shapeRenderer.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val circleRenderComponent = CircleRenderComponent.mapper.get(entity)
        tempMatrix4.setTranslation(circleRenderComponent.matrix4Track.getTranslation(tempVector3))
        tempMatrix4.translate(circleRenderComponent.vector3Offset)
        shapeRenderer.set(circleRenderComponent.shapeType)
        shapeRenderer.transformMatrix = tempMatrix4
        shapeRenderer.color = Color.GREEN
        shapeRenderer.rotate(1f, 0f, 0f, 90f)
        shapeRenderer.circle(0f, 0f, circleRenderComponent.radius)
    }
}