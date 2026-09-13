package net.wady.physics;

public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Vector vector) {
        if (this.x == vector.x) {
            if (this.y == vector.y) {
                return true;
            }
            else return false;
        }
        else return false;

    }
}
