public class PCRegistry {

    private static final int N = 10;
    private static final boolean[] OCCUPIED_FLAGS = new boolean[N];
    private static final long[] DURATION_DATA = new long[N];

    private PCRegistry() {
    }

    public static synchronized boolean isOccupied(int idx) {
        return false;
    }

    public static synchronized void occupy(int idx, long durationMs) {
    }

    public static synchronized void free(int idx) {
    }

    public static synchronized long getDurationMs(int idx) {
        return 0L;
    }

    public static synchronized void resetAll() {
    }
}