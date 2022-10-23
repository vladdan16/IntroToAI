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
        } else {
            runStatistics();
        }
        consoleWriter.close();
        consoleScanner.close();
    }

    private static void runStatistics() throws FileNotFoundException {
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
        map.displayMap(); // Displaying map to console

        String[][] data = new String[2][];
        data[0] = new String[2];
        data[1] = new String[2];

        // Starting perform Backtracking
        PrintWriter backtrackingWriter = new PrintWriter("outputBacktracking.txt");
        Algorithm backtracking = new Backtracking(map, backtrackingWriter);
        long start = System.currentTimeMillis();
        long startNs = System.nanoTime();
        data[0][1] = backtracking.compute();
        long end = System.currentTimeMillis();
        long endNs = System.nanoTime();
        backtrackingWriter.printf("%d ms\n", end - start);
        backtrackingWriter.close();
        consoleWriter.println("Backtracking algorithm is finished successfully. Execution time: " + (end - start) + "ms");
        data[0][0] = String.valueOf(endNs - startNs);

        //Starting perform AStar
        PrintWriter aStarWriter = new PrintWriter("outputAStar.txt");
        Algorithm aStar = new AStar(map, aStarWriter);
        start = System.currentTimeMillis();
        startNs = System.nanoTime();
        data[1][1] = aStar.compute();
        end = System.currentTimeMillis();
        endNs = System.nanoTime();
        aStarWriter.printf("%d ms\n", end - start);
        aStarWriter.close();
        consoleWriter.println("A* algorithm is finished successfully. Execution time: " + (end - start) + "ms");
        data[1][0] = String.valueOf(endNs - startNs);

        consoleWriter.flush();

        return data;
    }

    private static int readInput() {
        consoleWriter.println("Choose an input type:");
        consoleWriter.println("1. Generate the map and manually insert perception scenario from console");
        consoleWriter.println("2. Insert the positions of agents and perception scenario from the input.txt");
        consoleWriter.println("3. Run 1000 random tests to collect statistics");
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
            } else if (inputType == 3) {
                return inputType;
            }
            else {
                consoleWriter.println("Wrong input type. Choose 1, 2, or 3");
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

