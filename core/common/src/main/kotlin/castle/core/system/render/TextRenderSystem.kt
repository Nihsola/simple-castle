package castle.core.system.render

import castle.core.component.render.TextRenderComponent
import castle.core.service.CameraService
import castle.core.service.GameResources
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Align

class TextRenderSystem(
        private val spriteBatch: SpriteBatch,
        gameResources: GameResources,
        private val cameraService: CameraService
) : IteratingSystem(Family.all(TextRenderComponent::class.java).get()) {
    private val tempMat = Matrix4()
    private val textTransform = Matrix4().idt().rotate(0f, 1f, 0f, 90f)
    private val bitmapFont: BitmapFont = gameResources.bitmapFont

    override fun update(deltaTime: Float) {
        Gdx.gl.apply { glEnable(GL20.GL_DEPTH_TEST) }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textRenderComponent = TextRenderComponent.mapper.get(entity)
        val camera = cameraService.currentCamera.camera
        spriteBatch.projectionMatrix = tempMat.set(camera.combined).mul(textTransform).translate(textRenderComponent.offset)
        spriteBatch.begin()
        bitmapFont.draw(spriteBatch, textRenderComponent.text, 0f, 0f, 0f, Align.center, false)
        spriteBatch.end()
    }
}