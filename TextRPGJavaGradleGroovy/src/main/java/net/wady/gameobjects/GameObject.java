package net.wady.gameobjects;

import net.wady.physics.Vector;
import net.wady.rendering.PixelChar;
import net.wady.rendering.PixelColor;
import net.wady.rendering.Sprite;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameObject {
    private String name;
    private Vector position;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    private boolean hasStarted = false;

    public GameObject() {
        this.name = "GameObject_unnamed";
        this.position = new Vector(0,0);
    }

    public GameObject(String name, Vector position) {
        this.name = name;
        this.position = position;
    }

    // All Components (Sprite, Collider, etc...) implement the Component Interface, which allows us to create a system
    // based off of the Component Pattern (which relies on java.util.Optional, and a hashmap called 'components') to
    // create GameObjects with whichever components we choose.

    public GameObject addComponent(Component component) {
        components.put(component.getClass(), component);
        return this; // thus CHAINABLE!!! i.e. new GameObject("moose").addComponent(X).addComponent(Y)...
    }

    // the syntax below (<T extends Component> is a generic method type parameter, and just specifies that whatever T is, it must extend Component.
    // T is helpful because then we can return T. T is different from ? (wildcards), because you can't link the ? of wildcards to the return statement.
    public <T extends Component> Optional<T> getComponent(Class<T> type) {
        // Notice how here, we're returning an optional which contains Type T (so we must use T instead of ?)
        return Optional.ofNullable(type.cast(components.get(type)));
    }


    public boolean hasComponent(Class<? extends Component> type) {
        // Notice how here, we're simply returning a boolean value without returning type T (thus we can use wildcard  ? notation instead of t)
        return components.containsKey(type);
    }

    public void removeComponent(Class<? extends Component> type) {
        components.remove(type);
    }




    // --- START, AWAKE, UPDATE ---


    // --- Overriden by subclasses / components, default to doing nothing ---
    protected void onAwake() {};
    protected void onStart() {};
    protected void onUpdate(double deltaTime) {};


    // Called by the engine / scene, never overridden
    public final void awake() {
        onAwake();
        for (Component c : components.values()) {
            c.onAwake(this);
        }
    }

    public final void start() {
        if (hasStarted) return;
        hasStarted = true;
        onStart();
        for (Component c : components.values()) {
            c.onStart(this);
        }
    }

    public final void update(double deltaTime) {
        onUpdate(deltaTime);
        for (Component c : components.values()) {
            c.onUpdate(this, deltaTime);
        }
    }


    // --- GETTERS ---

    public String getName() {
        return name;
    }

    public Vector getPosition(){
        return position;
    }

}
