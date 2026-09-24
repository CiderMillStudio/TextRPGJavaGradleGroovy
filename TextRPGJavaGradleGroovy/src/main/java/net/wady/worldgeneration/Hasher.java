package net.wady.worldgeneration;

import java.util.List;
import java.util.SplittableRandom;


// This Hasher class serves to deterministically (repeatably) create pseudo-random values.
// It does so by relying on the splitmix64 and FNV-1a algorithms, and applications of it.
// It can take any sort of object or value, or a list of them, and can generate
// a unique long from those values that is entirely deterministic, yet offers so so many
// unique possibilities and combinations.

public final class Hasher {

    private Hasher() {}

    // Core Finalizer (borrowed from splitmix64)
    // Given ANY LONG, scrambles it very thoroughly.
    // This is the only has algorithm that is needed, because
    // everything else below is just a contributor for this
    // hash function
    public static long mix(long x) {
        x += 0x9E3779B97F4A7C15L;
        x = (x ^ (x >>> 30)) * 0xBF58476D1CE4E5B9L;
        x = (x ^ (x >>> 27)) * 0x94D049BB133111EBL;
        x ^= (x >>> 31);
        return x;
    }

    // Combine multiple longs into one before mixing:
    public static long combine (long... parts) {
        long h = 0x1234567890ABCDEFL; // arbitrary non-zero starting constant
        for (long p: parts) {
            h ^= mix(p + 0x9E3779B97F4A7C15L + (h << 6) + (h >>> 2));
        }
        return h;
    }


    // Turn a short tag string into a stable long (FNV-1a algorithm)
    // Lets us write hash(seed, x, y, "danger") instead of juggling integers
    // for every distinct "kind of roll" we want
    public static long tag(String s) {
        long h = 0xCBF29CE484222325L; // FNV offset basis

        for (int i = 0; i < s.length(); i++) {
            h ^= s.charAt(i);
            h *= 0x100000001B3L; // FNV prime
        }

        return h;
    }


    // Convenience Entry Point: has any mix of seed/coords/tags:
    // hash(worldSeed, level, regionX, regionY, "existence")
    // hash(flavorSeed, "guardian")
    public static long hash(long seed, Object... parts) {
        long[] longs = new long[parts.length + 1];
        longs[0] = seed;
        for (int i = 0; i < parts.length; i++) {
            Object p = parts[i];
            if (p instanceof String) longs[i + 1] = tag((String) p);
            else if (p instanceof Number) longs[i + 1] = ((Number) p).longValue();
            else longs[i + 1] = p.hashCode();
        }
        return combine(longs);
    }

    // --- Common derived helpers, so call sites read cleanly ---

    /* Uniform int in [0, bound). Correct for negative hashed values (unlike %).*/
    public static int nextInt(long hashed, int bound) {
        return (int) Math.floorMod(hashed, (long) bound);
    }

    /* Unifrom double in [0, 1) */
    public static double nextDouble(long hashed) {
        return (hashed >>> 11) * 0x1.0p-53; //top 53 bits, (full 'mantissa' precision)
    }

    /* True with given probability (0.0 - 1.0). for existence checks */
    public static boolean chance(long hashed, double probability) {
        return nextDouble(hashed) < probability;
    }

    /* Pick an item from a list deterministically */
    public static <T> T pick(long hashed, List<T> options) {
        return options.get(nextInt(hashed, options.size()));
    }

    /*
    * For EXPENSIVE generation needing many sequential decisions (for example,
    * laying out an entire dungeon floor's rooms/corridors/loot tables).
    * Seed a real PRNG just once instead of calling hash() dozens of times.
    * This is faster, and the streaming API (nextInt, nextDouble, etc...) is
    * nicer for "generate a hundred things" than hand-rolled tag strings.
    * SplittableRandom also supports .split() if you want independent child-
    * streams for sub-generators (e.g. one per room)
    * */
    public static SplittableRandom asRandom(long hashed) {
        return new SplittableRandom(hashed);
    }


}
