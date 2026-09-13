package net.wady.gameengine;


import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.Set;

public final class KeyInputListener {

    private final static KeyInputListener INSTANCE = new KeyInputListener();
    private final Set<KeyCode> heldKeys = EnumSet.noneOf(KeyCode.class);
    private KeyCode lastKeyPressed = KeyCode.COMMA;

    public void keyPressed(KeyCode e) {
        heldKeys.add(e);
        lastKeyPressed = e;
    }

    public void keyReleased(KeyCode e) {
        heldKeys.remove(e);
    }

    public boolean isKeyPressed(KeyCode e) {
        if (heldKeys.contains(e)) return true;
        else return false;
    }

    public static KeyInputListener getInstance() {
        return INSTANCE;
    }

    public Set<KeyCode> getHeldKeys() {
        return heldKeys;
    }

    public KeyCode getLastKeyPressed() { return lastKeyPressed; }
}
