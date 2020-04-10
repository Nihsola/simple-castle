package com.simple.castle.launcher.main.bullet.scene;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.simple.castle.launcher.main.bullet.controller.GameSelectItemController;
import com.simple.castle.launcher.main.bullet.controller.GameUnitSpawner;
import com.simple.castle.launcher.main.bullet.main.GameModels;
import com.simple.castle.launcher.main.bullet.main.GameObjectType;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.object.unit.SphereUnit;
import com.simple.castle.launcher.main.bullet.object.unit.controller.MainUnitController;
import com.simple.castle.launcher.main.bullet.physic.GamePhysicWorld;
import com.simple.castle.launcher.main.bullet.render.GameCamera;
import com.simple.castle.launcher.main.bullet.render.GameEnvironment;
import com.simple.castle.launcher.main.bullet.render.GameOverlay;
import com.simple.castle.launcher.main.bullet.render.GameRenderer;

import java.util.Map;

public class GameScene extends ScreenAdapter implements InputProcessor {

    private final Vector3 tempVector = new Vector3();

    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private final GameRenderer gameRenderer;
    private final GameModels gameModels;

    private Map<String, GameObject> sceneGameObjects;

    private GamePhysicWorld gamePhysicWorld;
    private GameEnvironment gameEnvironment;
    private GameCamera gameCamera;
    private GameOverlay gameOverlay;

    private GameUnitSpawner gameUnitSpawner;
    private MainUnitController mainUnitController;

    private GameSelectItemController gameSelectItemController;

    public GameScene(GameRenderer gameRenderer, GameModels gameModels) {
        this.gameRenderer = gameRenderer;
        this.gameModels = gameModels;
    }

    @Override
    public void render(float delta) {
        mainUnitController.update();

        gameCamera.update();

        gameRenderer.render(gameCamera, gamePhysicWorld, gameEnvironment);
        gamePhysicWorld.update(gameCamera, Math.min(1f / 30f, delta));
        gameOverlay.render(gameCamera, gameSelectItemController.getSelectedObject());
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void show() {
        gamePhysicWorld = new GamePhysicWorld();
        gamePhysicWorld.create();

        gameEnvironment = new GameEnvironment();
        gameEnvironment.create();

        gameOverlay = new GameOverlay();
        gameOverlay.create();

        sceneGameObjects = gameModels.constructNextModels(Map.ofEntries(
                Map.entry("Surface", GameObjectType.KINEMATIC),
                Map.entry("Castle-1", GameObjectType.KINEMATIC),
                Map.entry("Castle-2", GameObjectType.KINEMATIC),
                Map.entry("Castle-3", GameObjectType.KINEMATIC),
                Map.entry("Castle-4", GameObjectType.KINEMATIC),
                Map.entry("Spawner-1", GameObjectType.KINEMATIC)));

        Vector3 redCastlePosition = sceneGameObjects.get("Castle-1").transform.getTranslation(tempVector);

        gameCamera = new GameCamera();
        gameCamera.basePlane = sceneGameObjects.get("Surface");
        gameCamera.position.set(redCastlePosition.x + 10f, redCastlePosition.y + 10f, redCastlePosition.z);
        gameCamera.lookAt(redCastlePosition);

        gameUnitSpawner = new GameUnitSpawner(this);

        gameSelectItemController = new GameSelectItemController(gameCamera, gamePhysicWorld);

        mainUnitController = new MainUnitController();

        sceneGameObjects.forEach((s, gameObject) -> gamePhysicWorld.addRigidBody(gameObject));

        inputMultiplexer.addProcessor(gameSelectItemController);
        inputMultiplexer.addProcessor(gameUnitSpawner);
        inputMultiplexer.addProcessor(gameCamera);
    }

    @Override
    public void hide() {
        gamePhysicWorld.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputMultiplexer.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputMultiplexer.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputMultiplexer.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputMultiplexer.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputMultiplexer.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputMultiplexer.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputMultiplexer.scrolled(amount);
    }

    public void spawn() {
        SphereUnit build = new SphereUnit.Builder(gameModels.getMainModel()).build();

        build.body.setWorldTransform(new Matrix4());
        build.body.translate(sceneGameObjects.get("Spawner-1").transform.getTranslation(new Vector3()));

        gamePhysicWorld.addRigidBody(build);
    }

}