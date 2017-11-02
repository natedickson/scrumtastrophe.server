package roller;

import java.util.concurrent.ThreadLocalRandom;

public class Roll {
    public static int from(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }
}
