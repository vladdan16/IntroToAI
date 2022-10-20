import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter consoleWriter = new PrintWriter(System.out);
        Scanner consoleScanner = new Scanner(System.in);
        consoleWriter.println("Choose an input type:");
        consoleWriter.println("1. Generate the map and manually insert perception scenario from console");
        consoleWriter.println("2. Insert the positions of agents and perception scenario from the input.txt");
        consoleWriter.flush();
        Map map = new Map();
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
        // Now, we can perform our algorithms
        // Backtracking
        PrintWriter backtrackingWriter = new PrintWriter("outputBacktracking.txt");
        PrintWriter aStarWriter = new PrintWriter("outputAStar.txt");
        Algorithms algorithms = new Algorithms();
        long start = System.currentTimeMillis();
        algorithms.backtracking(map, backtrackingWriter);
        long end = System.currentTimeMillis();
        backtrackingWriter.printf("%d ms\n", end - start);
        backtrackingWriter.close();
        System.out.println("Backtracking algorithm is finished successfully. Execution time: " + (end - start) + "ms");
        start = System.currentTimeMillis();
        algorithms.aStar(map, aStarWriter);
        end = System.currentTimeMillis();
        aStarWriter.printf("%d ms\n", end - start);
        aStarWriter.close();
        System.out.println("A* algorithm is finished successfully. Execution time: " + (end - start) + "ms");
    }
}

