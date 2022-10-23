import java.io.PrintWriter;
import java.util.*;

/**
 * Class for Backtracking algorithm that extends Algorithm
 */
public class Backtracking extends Algorithm{
    private final int[][] minPathLengthArray;

    /**
     * A public constructor for Backtracking algorithm
     * @param map Map instance for algorithm
     * @param writer Writer instance for output
     */
    public Backtracking(Map map, PrintWriter writer) {
        super(map, writer);
        minPathLengthArray = new int[9][9];
        setMinPathLengthArray();
    }

    public void compute() {
        Set<Node> visited = new HashSet<>();
        Stack<Node> pathWithoutTortuga = traverse(visited, map.getJack(), map, map.getChest(), false);
        int pathLengthWithoutTortuga = pathWithoutTortuga.size() - 1;
        System.out.println("Algorithm without tortuga is finished");
        int pathLengthWithTortuga = 81;
        Stack<Node> pathWithTortuga = new Stack<>();
        if (pathLengthWithoutTortuga > Math.max(Math.abs(map.getJack().getX() - map.getTortuga().getX()), Math.abs(map.getJack().getY() - map.getTortuga().getY())) + Math.max(Math.abs(map.getTortuga().getX() - map.getChest().getX()), Math.abs(map.getTortuga().getY() - map.getChest().getY()))) {
            setMinPathLengthArray();
            pathWithTortuga = traverse(visited, map.getJack(), map, map.getTortuga(), false);
            pathLengthWithTortuga = pathWithTortuga.size() - 1;
            if (pathLengthWithTortuga < pathLengthWithoutTortuga) {
                setMinPathLengthArray();
                Stack<Node> pathAfterTortuga = traverse(visited, map.getTortuga(), map, map.getChest(), true);
                pathAfterTortuga.pop();
                pathWithTortuga.addAll(pathAfterTortuga);
                pathLengthWithTortuga = pathWithTortuga.size() - 1;
            }
        }
        if (Math.min(pathLengthWithoutTortuga, pathLengthWithTortuga) >= 80) {
            writer.println("Lose");
            return;
        }
        writer.println("Win");
        if (pathLengthWithoutTortuga <= pathLengthWithTortuga) {
            writer.println(pathLengthWithoutTortuga);
            displayResult(pathWithoutTortuga);
        } else {
            writer.println(pathLengthWithTortuga);
            displayResult(pathWithTortuga);
        }
    }

    private void setMinPathLengthArray() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                minPathLengthArray[i][j] = 81;
            }
        }
    }

    private Stack<Node> traverse(Set<Node> visited, Node node, Map map, Node dest, boolean tortuga) {
        if (node == dest) {
            Stack<Node> t = new Stack<>();
            t.push(node);
            minPathLengthArray[node.getX()][node.getY()] = visited.size();
            return t;
        }
        if (node.isRock()) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (tortuga && node.isKraken()) {
            map.disableKraken();
        }
        if (node.isEnemy()) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (visited.size() < minPathLengthArray[node.getX()][node.getY()]) {
            minPathLengthArray[node.getX()][node.getY()] = visited.size();
        } else {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        Set<Node> curVisited = new HashSet<>(visited);
        curVisited.add(node);

        int min = 9 * 9;
        Stack<Node> minPath = new Stack<>();
        for (int i = 0; i < 9 * 9; i++) {
            minPath.push(new Node(0, 0));
        }
        for (int x = Math.max(0, node.getX() - 1); x <= Math.min(8, node.getX() + 1); x++) {
            for (int y = Math.max(0, node.getY() - 1); y <= Math.min(8, node.getY() + 1); y++) {
                if (!curVisited.contains(map.getNode(x, y))) {
                    Stack<Node> t = traverse(curVisited, map.getNode(x, y), map, dest, tortuga);
                    int l = t.size();
                    if (l < min) {
                        min = l;
                        minPath.clear();
                        minPath.addAll(t);
                        if (l == Math.max(Math.abs(node.getX() - dest.getX()), Math.abs(node.getY() - dest.getY()))) {
                            x = 9;
                            break;
                        }
                    }
                }
            }
        }
        if (tortuga && node.isKraken())
            map.activateKraken();
        minPath.push(node);
        return minPath;
    }

    private void displayResult(Stack<Node> path) {
        Stack<Node> t = new Stack<>();
        t.addAll(path);
        while (!t.isEmpty()) {
            Node e = t.pop();
            writer.printf("[%d,%d] ", e.getX(), e.getY());
        }
        writer.println();
        writer.println("-------------------");
        writer.println("  0 1 2 3 4 5 6 7 8");
        for (int i = 0; i < 9; i++) {
            for (int j = -1; j < 9; j++) {
                if (j == -1) {
                    writer.printf("%d ", i);
                } else {
                    if (path.contains(map.getNode(i, j))) {
                        writer.printf("* ");
                    } else {
                        writer.printf("- ");
                    }
                }
            }
            writer.println();
        }
        writer.println("-------------------");
    }
}
