import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Main class of program
 */
public class Main {
    private static PrintWriter consoleWriter;
    private static Scanner consoleScanner;
    private static Map map;

    /**
     * An entry point of program
     * @param args Array of String
     * @throws FileNotFoundException if file was not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        consoleWriter = new PrintWriter(System.out);
        consoleScanner = new Scanner(System.in);
        map = new Map();
        int type = readInput();
        if (type == 1 || type == 2) {
            runAlgorithms();
        } else if (type == 3){
            collectStatistics();
        } else if (type == 4) {
            analyzeStatistics();
        } else if (type == 5) {
            checkMyAlgorithms();
        }
        consoleWriter.close();
        consoleScanner.close();
    }

    private static void checkMyAlgorithms() throws FileNotFoundException {
        consoleWriter.println("This is check of algorithms for 1 perception scenario");
        int k1 = 0;
        for (int i = 0; i < 1000; i++) {
            generateData(1);
            k1 = checkDifference(k1);
        }
        consoleWriter.printf("There was found %d differences for 1st perception scenario", k1);

        consoleWriter.flush();

        int k2 = 0;
        for (int i = 0; i < 1000; i++) {
            generateData(2);
            k2 = checkDifference(k2);
        }
        consoleWriter.printf("There was found %d differences for 2nd perception scenario", k2);

        if (k1 + k2 == 0) {
            consoleWriter.println("Thanks God! Your algorithms are ok!");
        }

        consoleWriter.flush();
    }

    private static int checkDifference(int k) throws FileNotFoundException {
        String[][] t = runAlgorithms();
        if (!t[0][1].equals(t[1][1])) {
            k++;
            consoleWriter.println("Algorithms give different result!");
            printInfo(t);
        } else if (!t[0][2].equals(t[1][2])) {
            k++;
            consoleWriter.println("Algorithms give same result, but different path lengths!");
            printInfo(t);
        }
        map.clear();
        return k;
    }

    private static void printInfo(String[][] t) {
        consoleWriter.printf("Backtracking: %s, AStar: %s\n", t[0][1], t[1][1]);
        consoleWriter.printf("Path lengths for Backtracking: %s, AStar: %s\n", t[0][2], t[1][2]);
        consoleWriter.println("Map for such case:");
        consoleWriter.flush();
        map.displayMap();
        consoleWriter.println("Test:");
        consoleWriter.flush();
        map.showData();
        consoleWriter.println();
    }

    private static void analyzeStatistics() throws FileNotFoundException {
        Scanner backtrackingScanner = new Scanner(new File("backtrackingData1.txt"));
        Scanner aStarScanner = new Scanner(new File("aStarData1.txt"));
        Statistics backtrackingStatistics = new Statistics(backtrackingScanner, consoleWriter);
        Statistics aStarStatistics = new Statistics(aStarScanner, consoleWriter);
        consoleWriter.println("Statistics for Backtracking with 1 perception scenario");
        backtrackingStatistics.calculate();
        consoleWriter.println("Statistics for A* with 1 perception scenario");
        aStarStatistics.calculate();

        backtrackingScanner.close();
        aStarScanner.close();

        backtrackingScanner = new Scanner(new File("backtrackingData2.txt"));
        aStarScanner = new Scanner(new File("aStarData2.txt"));
        backtrackingStatistics = new Statistics(backtrackingScanner, consoleWriter);
        aStarStatistics = new Statistics(aStarScanner, consoleWriter);
        consoleWriter.println("Statistics for Backtracking with 2 perception scenario");
        backtrackingStatistics.calculate();
        consoleWriter.println("Statistics for A* with 2 perception scenario");
        aStarStatistics.calculate();

        backtrackingScanner.close();
        aStarScanner.close();
        
    }

    private static void collectStatistics() throws FileNotFoundException {
        PrintWriter backtrackingDataWriter = new PrintWriter("backtrackingData1.txt");
        PrintWriter aStarDataWriter = new PrintWriter("aStarData1.txt");
        for (int i = 0; i < 1000; i++) {
            generateData(1);
            String[][] t = runAlgorithms();
            backtrackingDataWriter.printf("%s %s\n", t[0][0], t[0][1]);
            aStarDataWriter.printf("%s %s\n", t[1][0], t[1][1]);
            map.clear();
        }
        backtrackingDataWriter.close();
        aStarDataWriter.close();

        backtrackingDataWriter = new PrintWriter("backtrackingData2.txt");
        aStarDataWriter = new PrintWriter("aStarData2.txt");
        for (int i = 0; i < 1000; i++) {
            generateData(2);
            String[][] t = runAlgorithms();
            backtrackingDataWriter.printf("%s %s\n", t[0][0], t[0][1]);
            aStarDataWriter.printf("%s %s\n", t[1][0], t[1][1]);
            map.clear();
        }
        backtrackingDataWriter.close();
        aStarDataWriter.close();
    }

    private static String[][] runAlgorithms() throws FileNotFoundException {
        //map.displayMap(); // Displaying map to console

        String[][] data = new String[2][];

        data[0] = runBacktracking();

        data[1] = runAStar();

        consoleWriter.flush();

        return data;
    }

    private static String[] runBacktracking() throws FileNotFoundException {
        String[] t;
        String[] ans = new String[3];
        PrintWriter backtrackingWriter = new PrintWriter("outputBacktracking.txt");
        Algorithm backtracking = new Backtracking(map, backtrackingWriter);
        long start = System.currentTimeMillis();
        long startNs = System.nanoTime();
        t = backtracking.compute();
        long end = System.currentTimeMillis();
        long endNs = System.nanoTime();
        ans[1] = t[0];
        ans[2] = t[1];
        backtrackingWriter.printf("%d ms\n", end - start);
        backtrackingWriter.close();
        //consoleWriter.println("Backtracking algorithm is finished successfully. Execution time: " + (end - start) + "ms");
        ans[0] = String.valueOf(endNs - startNs);
        return ans;
    }

    private static String[] runAStar() throws FileNotFoundException {
        String[] t;
        String[] ans = new String[3];
        PrintWriter aStarWriter = new PrintWriter("outputAStar.txt");
        Algorithm aStar = new AStar(map, aStarWriter);
        long start = System.currentTimeMillis();
        long startNs = System.nanoTime();
        t = aStar.compute();
        long end = System.currentTimeMillis();
        long endNs = System.nanoTime();
        ans[1] = t[0];
        ans[2] = t[1];
        aStarWriter.printf("%d ms\n", end - start);
        aStarWriter.close();
        //consoleWriter.println("A* algorithm is finished successfully. Execution time: " + (end - start) + "ms");
        ans[0] = String.valueOf(endNs - startNs);
        return ans;
    }

    private static int readInput() {
        consoleWriter.println("Choose what you want to perform:");
        consoleWriter.println("1. Generate the map and manually insert perception scenario from console");
        consoleWriter.println("2. Insert the positions of agents and perception scenario from the input.txt");
        consoleWriter.println("3. Run 1000 random tests to collect statistics");
        consoleWriter.println("4. Analyze statistical data");
        consoleWriter.println("5. Run random tests and check if both algorithm works properly");
        consoleWriter.flush();
        while (true) {
            int inputType = consoleScanner.nextInt();
            if (inputType == 1) {
                generateData(0);
                return inputType;
            }
            else if (inputType == 2) {
                readFromFile();
                return inputType;
            } else if (inputType == 3 || inputType == 4 || inputType == 5) {
                return inputType;
            }
            else {
                consoleWriter.println("Wrong input type. Choose 1, 2, 3, 4, or 5");
                consoleWriter.flush();
            }
        }
    }

    private static void readFromFile() {
        try {
            Scanner inputScanner = new Scanner(new File("input.txt"));
            int[][] t = new int[6][2];
            for (int i = 0; i < 6; i++) {
                String s = inputScanner.next();
                String[] ss = s.substring(1, s.length() - 1).split(",");
                int[] tt = new int[2];
                tt[0] = Integer.parseInt(ss[0]);
                tt[1] = Integer.parseInt(ss[1]);
                t[i] = tt;
            }
            if (!map.setObjects(t)) {
                throw new FontFormatException("error");
            }
            int type = inputScanner.nextInt();
            if (type < 1 || type > 2) {
                throw new FontFormatException("error");
            }
            map.setPerceptionType(type);
        } catch (Exception e) {
            consoleWriter.println("Error in input.txt file! Check your input file and run this program again.");
            consoleWriter.flush();
            return;
        }
    }
    private static void generateData(int perceptionType) {
        map.generateObjects();
        if (perceptionType != 0) {
            map.setPerceptionType(perceptionType);
            return;
        }
        consoleWriter.println("Map is generated. Please, type perception scenario (1 or 2)");
        consoleWriter.flush();
        while (true) {
            int type = consoleScanner.nextInt();
            if (type >= 1 && type <= 2) {
                map.setPerceptionType(type);
                break;
            }
            consoleWriter.println("Wrong type. Choose 1 or 2");
            consoleWriter.flush();
        }
    }
}

