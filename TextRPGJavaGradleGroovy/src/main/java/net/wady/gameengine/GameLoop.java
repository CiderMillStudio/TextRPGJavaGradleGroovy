package net.wady.gameengine;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameLoop {

    int numFramesTillAutoStop = 0; // for testing;
    // The game loop takes in initialization input and then runs the game:


    public GameLoop() {

    }

    // A single dedicated background thread that will run our tick() method
    // on a schedule. One thread is enough -- we don't want ticks overlapping.
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    // A one-shot gate, starting at count 1. main() will wait on this;
    // it opens the moment stop() calls countDown().
    private final CountDownLatch doneLatch = new CountDownLatch(1);

    // Our "claim ticket" for the repeating scheduled task -- lets us cancel
    // future ticks. We never call get() on this since it's a repeating task
    // that (by design) never "completes" on its own.
    private ScheduledFuture<?> loopHandle;

    // volatile ensures that when one thread (e.g. the scheduler thread)
    // flips this flag, any other thread checking it sees the change
    // immediately, rather than reading a stale cached value.
    private volatile boolean running = false;

    // Begins the game loop, ticking at a fixed rate of 250ms (4 ticks/sec).
    public void start() {
        running = true;

        loopHandle = scheduler.scheduleAtFixedRate(
                this::tick,
                0,          // fire the first tick immediately
                250,                   // then every 250ms after that
                TimeUnit.MILLISECONDS
        );
    }

    // The heartbeat of the game. Runs once per tick, every 250ms.
    private void tick() {
        /*try {
            if (!running) return;

            // REAL GAME LOOP LOGIC GOES HERE
            // - Spawn GameObjects that need to be spawned INTO THE GAME LOGIC
            // - For any NEW GameObjects , execute their OnAwake methods and their Start methods
            // - Run all GameObjects' UPDATE methods
            // - Feed all GameObjects' updated data into a RESOLUTION_FUNNEL
            // - RESOLUTION_FUNNEL processes data and events, including collisions, interactions, deletions
            // - sort through logic of where things should be on the screen
            // - Export Visual Data to SCREEN_RENDERER, which renders the screen within the game loop
            // - listen for input for next frame


            FrameOutput frame = new FrameOutput(30, 90, pixelCharGenerator());
            frame.printFrame();

            numFramesTillAutoStop++;



            // check whatever condition means "the game is over"
            if (shouldStop()) {
                stop();
            }

            if (numFramesTillAutoStop >= 4)
                stop();

        } catch (Exception e) {
            // If a tick throws unhandled, the scheduler would otherwise
            // silently cancel all future ticks with no obvious symptom.
            // We catch it, log it, and stop cleanly instead.
            e.printStackTrace();
            stop();
        }
    }

    // The 'return false' is a placeholder -- wire this to actual exit conditions
    private boolean shouldStop() {

        return false;


    }

    // Halts the game loop and releases anything waiting on awaitCompletion().
    public void stop() {
        running = false;

        if (loopHandle != null) {
            // false = don't forcibly interrupt a currently-running tick;
            // just prevent future ticks from firing.
            loopHandle.cancel(false);
        }

        scheduler.shutdown();

        // Drop the latch count from 1 to 0 -- this releases any thread
        // blocked in awaitCompletion(), e.g. main().
        doneLatch.countDown();

    }


    // Blocks the calling thread until stop() has been called.
    // Intended to be called from main() so it doesn't proceed
    // until the game loop has truly finished.
    public void awaitCompletion() throws InterruptedException {
        doneLatch.await();
    }


    // just testing.
    public static List<PixelChar> pixelCharGenerator() {

        List<PixelChar> pixels = new ArrayList<>();

        int i = 0;

        while (i <= (30 * 90)) {
            PixelChar pixel = new PixelChar('0', 0, PixelColor.RED);
            pixels.add(pixel);
            i++;
        }

        return pixels;
        }
         */

    }

}
