package net.wady.player;

import javafx.scene.input.KeyCode;
import net.wady.gameengine.KeyInputListener;
import net.wady.gameobjects.Component;
import net.wady.gameobjects.GameObject;
import net.wady.physics.Vector;

import java.security.Key;

public class PlayerController implements Component {

    private final int SLOW_MOVES_PER_SECOND = 2;
    private final int CHAIN_MOVE_FACTOR_MEDIUM_SPEED = 4;
    private final int CHAIN_MOVE_FACTOR_FAST_SPEED = 8;
    private final int CHAIN_MOVE_FACTOR_MAX_SPEED = 16;

    private final long NS_PER_MOVE = 1_000_000_000l / SLOW_MOVES_PER_SECOND;

    private long timeSpentHoldingSameKey = 0;
    private long timeSinceLastMove = 0;
    private boolean canAttemptMove = true;
    private int chainMoveFactor = 1;

    private Vector latestMoveDir = new Vector(0,0);

    @Override
    public void onUpdate(GameObject owner, long deltaTime) {
        Component.super.onUpdate(owner, deltaTime);

        timeSinceLastMove += deltaTime;

        // if (timeSinceLastMove > NS_PER_MOVE + 200_000_000l) timeSpentHoldingSameKey = 0;


        if (timeSinceLastMove > (NS_PER_MOVE / chainMoveFactor)) canAttemptMove = true;


        if (KeyInputListener.getInstance().getHeldKeys().size() == 0) {
            timeSpentHoldingSameKey = 0;
            chainMoveFactor = 1;
            canAttemptMove = true;
        }

        if (canAttemptMove) {
            AttemptMove(owner);
        }


    }

    private void MoveController(GameObject owner, KeyInputListener listener, KeyCode keycode, int xDir, int yDir) {
        if (listener.isKeyPressed(keycode)) {
            if (listener.getLastKeyPressed().equals(keycode)) {
                timeSpentHoldingSameKey += (NS_PER_MOVE / chainMoveFactor);
                if (timeSpentHoldingSameKey > NS_PER_MOVE * 8) chainMoveFactor = CHAIN_MOVE_FACTOR_MAX_SPEED;
                else if (timeSpentHoldingSameKey > NS_PER_MOVE * 4) chainMoveFactor = CHAIN_MOVE_FACTOR_FAST_SPEED;
                else if (timeSpentHoldingSameKey > NS_PER_MOVE) chainMoveFactor = CHAIN_MOVE_FACTOR_MEDIUM_SPEED;
                else chainMoveFactor = 1;
            }
            else {
                chainMoveFactor = 1;
                timeSpentHoldingSameKey = 0;
            }

            move(owner, xDir, yDir);
        }
    }

    private void AttemptMove(GameObject owner) {

        KeyInputListener keyListener = KeyInputListener.getInstance();

        Vector newMoveDir;

        if (keyListener.getLastKeyPressed().equals(KeyCode.W)) newMoveDir = new Vector(0, 1);
        else if (keyListener.getLastKeyPressed().equals(KeyCode.S)) newMoveDir = new Vector(0, -1);
        else if (keyListener.getLastKeyPressed().equals(KeyCode.A)) newMoveDir = new Vector(-1, 0);
        else if (keyListener.getLastKeyPressed().equals(KeyCode.D)) newMoveDir = new Vector(1, 0);
        else newMoveDir = new Vector(0, 0);


        if (latestMoveDir.equals(newMoveDir) == false) {
            chainMoveFactor = 1;
            timeSpentHoldingSameKey = 0;
        }




        MoveController(owner, keyListener, keyListener.getLastKeyPressed(), newMoveDir.getX(), newMoveDir.getY());

    }

    private void move(GameObject owner, int xDir, int yDir) {

        Vector newMoveDir = new Vector(xDir, yDir);

        ((Player) owner).move(new Vector(newMoveDir.getX(), -1 * newMoveDir.getY())); // -1 * yDir because for some reason, top-left cell is (0,0), and y increases as you go DOWN.

        latestMoveDir = newMoveDir;
        timeSinceLastMove = 0;
        canAttemptMove = false;
    }


}
