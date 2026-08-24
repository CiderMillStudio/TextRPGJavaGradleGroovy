package net.wady.gameobjects;

public interface Component {
    default void onAwake(GameObject owner) { }
    default void onStart(GameObject owner) { }
    default void onUpdate(GameObject owner) { }
}
