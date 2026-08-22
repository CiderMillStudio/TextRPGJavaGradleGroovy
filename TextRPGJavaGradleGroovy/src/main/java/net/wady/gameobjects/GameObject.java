package net.wady.gameobjects;

import net.wady.physics.Vector;
import net.wady.rendering.PixelChar;
import net.wady.rendering.PixelColor;
import net.wady.rendering.Sprite;

public class GameObject {
    private String name;
    private Vector position;
    private Sprite sprite;



    public GameObject() {
        this.name = "GameObject_unnamed";
        this.position = new Vector(0,0);

    }

    public GameObject(String name, Vector position) {
        this.name = name;
        this.position = position;
        this.sprite = new Sprite(new PixelChar('#',0, PixelColor.BLUE));
    }

    public GameObject(String name, Vector position, Sprite sprite) {
        this.name = name;
        this.position = position;
        this.sprite = sprite;
    }

    public String getName() {
        return name;
    }

    public Vector getPosition(){
        return position;
    }




}
