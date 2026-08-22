package net.wady.physics;

import java.util.ArrayList;
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
    // determines whether this collidability mask acts as a barrier or as a trigger to detect another collider passing through it.
    private boolean isTrigger = false;



    // --- COLLIDER CONSTRUCTORS ---

    // makes a 1x1 collidable mask:
    public Collider() {
        this.size = new Vector(1,1);
        this.collidabilityMatrix = new boolean[] {true};
        this.isTrigger = false;
    }

    // makes a custom-sized collider mask where all the pixels are collidable:
    public Collider(Vector size) {
        this.size = size;

        List<Boolean> collidabilityMatrix = new ArrayList();

        this.collidabilityMatrix = new boolean[] {true};
        this.isTrigger = false;
    }

    public Collider(Vector size, boolean[] collidabilityMatrix) {

    }






}
