package com.simple.castle.launcher.main.bullet.main;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.ArrayMap;
import com.simple.castle.launcher.main.bullet.object.GameObjectConstructor;
import com.simple.castle.launcher.main.utils.GameObjectsUtil;

import java.util.*;
import java.util.function.Function;

public class GameModels {

    private final Model mainModel;
    private final ArrayMap<String, GameObjectConstructor> constructors;

    private final Map<List<Object>, Function<BoundingBox, btCollisionShape>> nodeShape = new HashMap<>(Map.ofEntries(
            Map.entry(Arrays.asList(0f, "Surface"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(1f, "Unit-1"), GameObjectsUtil::calculateSphere),
            Map.entry(Arrays.asList(0f, "Castle-1"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-2"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-3"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Castle-4"), GameObjectsUtil::calculateBox),

            Map.entry(Arrays.asList(0f, "Area-Left-Down"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Area-Left-Up"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Area-Right-Down"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Area-Right-Up"), GameObjectsUtil::calculateBox),

            Map.entry(Arrays.asList(0f, "Spawner-Red-Left"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Red-Right"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Red-Up"), GameObjectsUtil::calculateBox),

            Map.entry(Arrays.asList(0f, "Spawner-Blue-Down"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Blue-Left"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Blue-Up"), GameObjectsUtil::calculateBox),

            Map.entry(Arrays.asList(0f, "Spawner-Green-Down"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Green-Left"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Green-Right"), GameObjectsUtil::calculateBox),

            Map.entry(Arrays.asList(0f, "Spawner-Teal-Down"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Teal-Left"), GameObjectsUtil::calculateBox),
            Map.entry(Arrays.asList(0f, "Spawner-Teal-Up"), GameObjectsUtil::calculateBox)));

    public GameModels(Model mainModel) {
        this.mainModel = mainModel;
        constructors = new ArrayMap<>();

        Set<String> nodes = new HashSet<>();
        mainModel.nodes.forEach(node -> nodes.add(node.id));

        for (int i = 0; i < 800; i++) {
            String format = String.format("Cone.%03d", i);
            if (nodes.contains(format)) {
                nodeShape.put(Arrays.asList(0f, format), GameObjectsUtil::calculateBox);
            }
        }
        constructors.putAll(constructObjects());
    }

    private ArrayMap<String, GameObjectConstructor> constructObjects() {
        final BoundingBox tmp = new BoundingBox();
        ArrayMap<String, GameObjectConstructor> constructors = new ArrayMap<>(String.class, GameObjectConstructor.class);
        nodeShape.forEach((array, boundingBoxbtCollisionShapeFunction) -> {
                    float mass = (float) array.get(0);
                    String node = (String) array.get(1);
                    btCollisionShape shape = boundingBoxbtCollisionShapeFunction.apply(mainModel.getNode(node).calculateBoundingBox(tmp));
                    constructors.put(node, new GameObjectConstructor(mainModel, node, shape, mass));
                }
        );
        return constructors;
    }

    public void dispose() {
        for (GameObjectConstructor constructor : constructors.values()) {
            constructor.dispose();
        }
        constructors.clear();
    }

    public ArrayMap<String, GameObjectConstructor> getConstructors() {
        return constructors;
    }
}
