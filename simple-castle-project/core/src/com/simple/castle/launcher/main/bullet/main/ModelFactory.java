package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.object.GameObject;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.utils.GameObjectsUtil;
import com.simple.castle.launcher.main.utils.ModelLoader;

import java.util.Arrays;
import java.util.List;

public class ModelFactory extends ApplicationAdapter {
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;
    private final static short ALL_FLAG = -1;

    private final ArrayMap<String, GameObjectConstructor> constructorsArrayMap = new ArrayMap<>();
    private Model mainModel;

    private List<GameObject> initObjects;

    private GameObject redCastle;
    private GameObject surface;
    private GameObject spawner;

    public GameObject getSpawner() {
        return spawner;
    }

    public Model getMainModel() {
        return mainModel;
    }

    @Override
    public void create() {
        mainModel = ModelLoader.loadModel();
        constructorsArrayMap.putAll(constructObjects());
        initObjects = constructMainObjects();
    }

    private ArrayMap<String, GameObjectConstructor> constructObjects() {
        BoundingBox tmp = new BoundingBox();

        String surface = "Surface";
        String unit1 = "Unit-1";
        String castle1 = "Castle-1";
        String castle2 = "Castle-2";
        String castle3 = "Castle-3";
        String castle4 = "Castle-4";
        String spawner1 = "Spawner-1";

        ArrayMap<String, GameObjectConstructor> constructors = new ArrayMap<>(String.class, GameObjectConstructor.class);
        constructors.put(surface, new GameObjectConstructor(mainModel, surface,
                GameObjectsUtil.calculateBox(mainModel.getNode(surface).calculateBoundingBox(tmp)), 0f));
        constructors.put(castle1, new GameObjectConstructor(mainModel, castle1,
                GameObjectsUtil.calculateBox(mainModel.getNode(castle1).calculateBoundingBox(tmp)), 0f));
        constructors.put(castle2, new GameObjectConstructor(mainModel, castle2,
                GameObjectsUtil.calculateBox(mainModel.getNode(castle2).calculateBoundingBox(tmp)), 0f));
        constructors.put(castle3, new GameObjectConstructor(mainModel, castle3,
                GameObjectsUtil.calculateBox(mainModel.getNode(castle3).calculateBoundingBox(tmp)), 0f));
        constructors.put(castle4, new GameObjectConstructor(mainModel, castle4,
                GameObjectsUtil.calculateBox(mainModel.getNode(castle4).calculateBoundingBox(tmp)), 0f));
        constructors.put(unit1, new GameObjectConstructor(mainModel, unit1,
                GameObjectsUtil.calculateSphere(mainModel.getNode(unit1).calculateBoundingBox(tmp)), 1f));

        constructors.put(spawner1, new GameObjectConstructor(mainModel, spawner1,
                GameObjectsUtil.calculateBox(mainModel.getNode(spawner1).calculateBoundingBox(tmp)), 0f));
        return constructors;
    }

    private List<GameObject> constructMainObjects() {
        redCastle = constructStaticObject("Castle-1");
        surface = constructStaticObject("Surface");
        spawner = constructObject("Spawner-1");

        return Arrays.asList(spawner,
                redCastle,
                constructStaticObject("Castle-2"),
                constructStaticObject("Castle-3"),
                constructStaticObject("Castle-4"),
                surface);
    }

    public GameObject constructStaticObject(String value) {
        GameObject object = this.getGameObjectsConstructors().get(value).construct();
        object.body.setCollisionFlags(object.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        object.body.setContactCallbackFlag(GROUND_FLAG);
        object.body.setContactCallbackFilter(0);
        object.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        return object;
    }

    public GameObject constructObject(String value) {
        GameObject obj = this.getGameObjectsConstructors().get(value).construct();
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(GROUND_FLAG);
        return obj;
    }

    public void dispose() {
        for (GameObjectConstructor constructor : constructorsArrayMap.values()) {
            constructor.dispose();
        }
        constructorsArrayMap.clear();
        mainModel.dispose();
    }

    public ArrayMap<String, GameObjectConstructor> getGameObjectsConstructors() {
        return constructorsArrayMap;
    }

    public List<GameObject> getInitObjects() {
        return initObjects;
    }

    public GameObject getRedCastle() {
        return redCastle;
    }

    public GameObject getSurface() {
        return surface;
    }
}
