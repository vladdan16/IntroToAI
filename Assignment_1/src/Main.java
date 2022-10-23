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
        runProgram();
        consoleWriter.close();
        consoleScanner.close();
    }

    private static void runProgram() throws FileNotFoundException {
        map = new Map();

        // Get input by type that user prefer
        readInput();
        // Now, we can perform our algorithms
        map.displayMap(); // Displaying map to console

        // Starting perform Backtracking
        PrintWriter backtrackingWriter = new PrintWriter("outputBacktracking.txt");
        Algorithm backtracking = new Backtracking(map, backtrackingWriter);
        long start = System.currentTimeMillis();
        backtracking.compute();
        long end = System.currentTimeMillis();
        backtrackingWriter.printf("%d ms\n", end - start);
        backtrackingWriter.close();
        consoleWriter.println("Backtracking algorithm is finished successfully. Execution time: " + (end - start) + "ms");

        //Starting perform AStar
        PrintWriter aStarWriter = new PrintWriter("outputAStar.txt");
        Algorithm aStar = new AStar(map, aStarWriter);
        start = System.currentTimeMillis();
        aStar.compute();
        end = System.currentTimeMillis();
        aStarWriter.printf("%d ms\n", end - start);
        aStarWriter.close();
        consoleWriter.println("A* algorithm is finished successfully. Execution time: " + (end - start) + "ms");

        consoleWriter.flush();
    }

    private static void readInput() {
        consoleWriter.println("Choose an input type:");
        consoleWriter.println("1. Generate the map and manually insert perception scenario from console");
        consoleWriter.println("2. Insert the positions of agents and perception scenario from the input.txt");
        consoleWriter.flush();
        while (true) {
            int inputType = consoleScanner.nextInt();
            if (inputType == 2) {
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
                break;
            } else if (inputType == 1) {
                map.generateObjects();
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
                break;
            } else {
                consoleWriter.println("Wrong input type. Choose 1 or 2");
                consoleWriter.flush();
            }
        }
    }
}

