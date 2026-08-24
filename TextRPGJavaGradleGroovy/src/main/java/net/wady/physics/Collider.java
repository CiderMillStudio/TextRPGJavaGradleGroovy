package net.wady.physics;

import net.wady.gameobjects.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collider implements Component {

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



    // --- USE THE BUILDER PATTERN INSTEAD OF MANY CONSTRUCTORS ---

    private Collider(Builder b) {
        this.size = b.size;
        this.collidabilityMatrix = b.collidabilityMatrix;
        this.isTrigger = b.isTrigger;
    }

    // The Builder Class will be nested within the Collider class (this way they can share private parameters, and this
    // Builder class can only be called via Collider.Builder (enhances privacy, ensures that other objects can't call this Builder class).
    public static class Builder {
        // Notice how Builder's parameters are PRIVATE, yet the outside class (Collider) can still access them as if they're public.
        // This is because that when classes are nested within another class, they share access to their private fields
        // Thus, Builder (The inside class) has access to Collider's private fields, and vice versa
        private Vector size = new Vector (1,1);
        private boolean[] collidabilityMatrix;
        private boolean isTrigger = false;

        public Builder size(Vector size) {
            this.size = size;
            return this; // CHAINABLE
        }

        public Builder collidabilityMatrix(boolean[] collidabilityMatrix) {
            this.collidabilityMatrix = collidabilityMatrix;
            return this; // CHAINABLE
        }

        public Builder isTrigger(boolean isTrigger) {
            this.isTrigger = isTrigger;
            return this; // CHAINABLE
        }

        public Collider build() {
            if (collidabilityMatrix == null) {
                collidabilityMatrix = new boolean[size.getX() * size.getY()];
                Arrays.fill(collidabilityMatrix, true);
            }
            return new Collider(this);
        }


        // An example of how one might build a Collider:

        /*
        Collider wall = new Builder()
                .size(new Vector(1, 3))
                .isTrigger(false)
                .build();
        */

        // notice that if collidabilityMatrix is not incorporated, it is fully collidable by default.
        // likewise, isTrigger and size also have default values of false and (1, 1), respectively.

    }






    // --- COLLIDER METHODS ---










}
