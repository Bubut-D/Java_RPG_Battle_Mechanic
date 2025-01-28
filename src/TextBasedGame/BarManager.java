package TextBasedGame;

public class BarManager {
    private static final int MAX_BARS = 15;
    private static final String[] BAR_CACHE = new String[16];
    private static final char FILLED = '█';
    private static final char EMPTY = '░';

    static {
        for (int i = 0; i < 16; i++) {
            StringBuilder bar = new StringBuilder(MAX_BARS);
            for (int j = 0; j < i; j++) {
                bar.append(FILLED);
            }
            for (int j = i; j < MAX_BARS; j++) {
                bar.append(EMPTY);
            }
            BAR_CACHE[i] = bar.toString();
        }
    }

    public static String getBar(int value, int max){
        try {
            if (value > max)
                throw new IllegalArgumentException("Illegal Bar Argument");

            value = Math.max(0,(int)Math.ceil(((float)value)*MAX_BARS/max));
            return BAR_CACHE[value];
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
}