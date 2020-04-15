package com.simple.castle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simple.castle.launcher.main.bullet.main.GameLauncher;

public class DesktopLauncher {
    public static void main(String[] arg) {
//		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        new LwjglApplication(new GameLauncher(), config);
    }
}
