package net.wady.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collider {

    // --- COLLIDER PARAMETERS ---

    // Collider.size:
    // the x and y dimensions of the collider mask
    private Vector size;

    // Collider.collidabilityMatrix:
    // the pixel-by-pixel determiner of whether each pixel can be collided with (true), or can be traversed through (false) by another collider.
    // For example, an L shaped wall might have a size of 3x3 but only the left-most 3 (0, 3, 6) and the bottom-most 3 (6 ,7, 8) would actually be collidable.
    // for this example, the collidability matrix would be:
    // {true, false, false,
    //  true, false, false,
    //  true, true,  true}
    private boolean[] collidabilityMatrix;

    // Collider.isTrigger
    // determines whether this collidability mask acts as a barrier (false) or as a trigger (true) to detect another collider passing through it.
    private boolean isTrigger = false;



    // --- COLLIDER CONSTRUCTORS ---

    // makes a 1x1 collidable mask that is NOT a trigger:
    public Collider() {
        this.size = new Vector(1,1);
        this.collidabilityMatrix = new boolean[] {true};
        this.isTrigger = false;
    }

    // makes a custom-sized collider mask where all the pixels are collidable, assumes isTrigger is false:
    public Collider(Vector size) {
        this.size = size;

        boolean[] collidabilityMatrix = new boolean[size.getX() * size.getY()];
        Arrays.fill(collidabilityMatrix, true);
        this.collidabilityMatrix = collidabilityMatrix;

        this.isTrigger = false;
    }

    // makes a custom-sized collider mask with a customized collidability matrix:
    public Collider(Vector size, boolean[] collidabilityMatrix) {
        this.size = size;
        this.collidabilityMatrix = collidabilityMatrix;
        this.isTrigger = false;
    }


    // makes a custom-sized collidability mask with a customized collidability matrix with the option of enabling
    // isTrigger mode. This allows you to have the collider be a trigger with a custom shape instead of a solid
    // rectangle
    public Collider(Vector size, boolean[] collidabilityMatrix, boolean isTrigger) {
        this.size = size;
        this.collidabilityMatrix = collidabilityMatrix;
        this.isTrigger = isTrigger;
    }

    // makes a 1 x 1 collider with the option of it being isTrigger mode.
    public Collider (boolean isTrigger) {
        this.size = new Vector(1,1);
        this.collidabilityMatrix = new boolean[] {true};
        this.isTrigger = isTrigger;
    }

    // makes a custom-sized collidable/triggerable mask where all units are collidable/triggerable
    public Collider(Vector size, boolean isTrigger) {
        this.size = size;
        this.isTrigger = isTrigger;

        boolean[] collidabilityMatrix = new boolean[size.getX() * size.getY()];
        Arrays.fill(collidabilityMatrix, true);
        this.collidabilityMatrix = collidabilityMatrix;


    }





    // --- COLLIDER METHODS ---










}
