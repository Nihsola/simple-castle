package com.simple.castle.manager.impl;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

public abstract class Scene implements ApplicationListener, InputProcessor {

    private InputProcessor inputProcessor;
    private ManagerContext managerContext;

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    public void update() {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return inputProcessor != null && inputProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputProcessor != null && inputProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return inputProcessor != null && inputProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null && inputProcessor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputProcessor != null && inputProcessor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputProcessor != null && inputProcessor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return inputProcessor != null && inputProcessor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return inputProcessor != null && inputProcessor.scrolled(amount);
    }

    public void settingUpdated(String name, String value) {

    }

    protected void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public ManagerContext getManagerContext() {
        if (managerContext == null) {
            throw new IllegalStateException("There is no context");
        }
        return managerContext;
    }

    public void setManagerContext(ManagerContext managerContext) {
        this.managerContext = managerContext;
    }

}
