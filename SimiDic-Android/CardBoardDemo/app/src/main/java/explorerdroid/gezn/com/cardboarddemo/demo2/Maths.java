package explorerdroid.gezn.com.cardboarddemo.demo2;

/**
 * Created by pteran on 23-03-16.
 */
public final class Maths {

    /**
     * 180 in radians.
     */
    public static final double ONE_EIGHTY_DEGREES = Math.PI;

    /**
     * 360 in radians.
     */
    public static final double THREE_SIXTY_DEGREES = ONE_EIGHTY_DEGREES * 2;

    /**
     * 120 in radians.
     */
    public static final double ONE_TWENTY_DEGREES = THREE_SIXTY_DEGREES / 3;

    /**
     * 90 degrees, North pole.
     */
    public static final double NINETY_DEGREES = Math.PI / 2;

    /**
     * Used by power.
     */
    private static final long POWER_CLAMP = 0x00000000ffffffffL;

    /**
     * Constructor, although not used at the moment.
     */
    private Maths() {
    }

    /**
     * Quick integer power function.
     *
     * @param base  number to raise.
     * @param raise to this power.
     * @return base ^ raise.
     */
    public static int power(final int base, final int raise) {
        int p = 1;
        long b = raise & POWER_CLAMP;

        // bits in b correspond to values of powerN
        // so start with p=1, and for each set bit in b, multiply corresponding
        // table entry
        long powerN = base;

        while (b != 0) {
            if ((b & 1) != 0) {
                p *= powerN;
            }
            b >>>= 1;
            powerN = powerN * powerN;
        }

        return p;
    }
}