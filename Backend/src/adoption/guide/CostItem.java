package adoption.guide;

public class CostItem {
    public enum Frequency {
        ONE_TIME("one-time"), MONTHLY("per month"), PER_SERVICE("per service");

        private final String label;
        Frequency(String label) { 
            this.label = label; 
        }
        @Override public String toString() { 
            return label; 
        }
    }

    private final String name;
    private final double min;
    private final double max;
    private final Frequency frequency;
    private final boolean openEnded;

    public CostItem(String name, double min, double max, Frequency frequency, boolean openEnded) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid cost range for" + name + ".");  
        }
        this.name = name;
        this.min = min;
        this.max = max;
        this.frequency = frequency;
        this.openEnded = openEnded;
    }

    public CostItem(String name, double min, double max, Frequency frequency) {
        this(name, min, max, frequency, false);
    }

    public CostItem(String name, double price, Frequency frequency) {
        this(name, price, price, frequency, false);
    }

    public String getName()          { return name; }
    public double getMin()           { return min; }
    public double getMax()           { return max; }
    public Frequency getFrequency()  { return frequency; }
    public boolean isOpenEnded()     { return openEnded; }

    public String getRangeText() {
        return formatRange(min, max, openEnded);
    }

    public static String formatRange(double min, double max, boolean openEnded) {
        String text = (min == max)
                ? String.format("PHP %,.0f", min)
                : String.format("PHP %,.0f - %,.0f", min, max);
        return openEnded ? text + "+" : text;
    }
}