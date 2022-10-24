import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A class for analyzing statistics
 */
class Statistics {
    private final Scanner scanner;
    private final PrintWriter writer;
    private final String[][] data;

    /**
     * A public constructor that accepts from where we need read data and to which output write it
     * @param scanner Scanner instance
     * @param writer PrintWriter instance
     */
    public Statistics(Scanner scanner, PrintWriter writer) {
        this.scanner = scanner;
        this.writer = writer;
        data = new String[1000][2];
    }

    /**
     * A public void to calculate statistical data
     */
    public void calculate() {
        if (!readData()) {
            writer.println("Data file is missing or has errors");
            return;
        }
        writer.printf("Mean: %f\n", calculateMean());
        writer.printf("Mode: %f\n", calculateMode());
        writer.printf("Standard deviation: %f\n", calculateStandardDeviation());
        writer.printf("Loses: %d\n", numberOfLoses());
        writer.printf("Wins: %d\n", numberOfWins());
        writer.println();
        writer.flush();
    }

    private boolean readData() {
        try {
            for (int i = 0; i < 1000; i++) {
                String[] t = new String[2];
                t[0] = scanner.next();
                t[1] = scanner.next();
                data[i] = t;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private double calculateMean() {
        double ans = 0;
        for (String[] datum : data) {
            ans += Integer.parseInt(datum[0]);
        }
        ans /= data.length;
        ans /= 1000000;
        return ans;
    }

    private double calculateStandardDeviation() {
        double mean = calculateMean();
        double ans = 0;
        for (String[] datum : data) {
            ans += Math.pow(Integer.parseInt(datum[0]) - mean, 2);
        }
        ans /= data.length - 1;
        ans /= Math.pow(10, 12);
        return Math.sqrt(ans);
    }

    private double calculateMode() {
        Map<Integer, Integer> map = new HashMap<>();
        for (String[] datum : data) {
            Integer t = Integer.parseInt(datum[0]) / 10000;
            if (!map.containsKey(t)) {
                map.put(t, 1);
            } else {
                map.put(t, map.get(t) + 1);
            }
        }
        int max = 0;
        double ans = 0;
        for (Integer key : map.keySet()) {
            if (map.get(key) > max) {
                max = map.get(key);
                ans = key;
            }
        }
        return ans / 100;
    }

    private int numberOfLoses() {
        int res = 0;

        for (String[] datum : data) {
            if (datum[1].equals("L")) res++;
        }
        return res;
    }
    
    private int numberOfWins() {
        int res = 0;
        
        for (String[] datum : data) {
            if (datum[1].equals("W")) res++;
        }
        return res;
    }
}
