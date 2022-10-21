import java.io.PrintWriter;
import java.util.*;

public class Algorithms {
    Stack<Node> pathBacktracking;
    int[][] minPathLengthArray;

    public Algorithms() {
        pathBacktracking = new Stack<>();
        minPathLengthArray = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                minPathLengthArray[i][j] = 81;
            }
        }
    }

    public void backtracking(Map map, PrintWriter backtrackingWriter) {
        map.displayMap();
        Set<Node> visited = new HashSet<>();
        pathBacktracking.push(map.getNode(0, 0));
        Stack<Node> pathWithoutTortuga = traverse(visited, map.getJack(), map, map.getChest(), false);
        int pathLengthWithoutTortuga = pathWithoutTortuga.size() - 1;
        System.out.println("Algorithm without tortuga is finished");
        int pathLengthWithTortuga = 81;
        Stack<Node> pathWithTortuga = new Stack<>();
        if (pathLengthWithoutTortuga > Math.max(Math.abs(map.getJack().x - map.getTortuga().x), Math.abs(map.getJack().y - map.getTortuga().y)) + Math.max(Math.abs(map.getTortuga().x - map.getChest().x), Math.abs(map.getTortuga().y - map.getChest().y))) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    minPathLengthArray[i][j] = 81;
                }
            }
            pathWithTortuga = traverse(visited, map.getJack(), map, map.getTortuga(), false);
            pathLengthWithTortuga = pathWithTortuga.size() - 1;
            if (pathLengthWithTortuga < pathLengthWithoutTortuga) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        minPathLengthArray[i][j] = 81;
                    }
                }
                Stack<Node> pathAfterTortuga = traverse(visited, map.getTortuga(), map, map.getChest(), true);
                pathAfterTortuga.pop();
                pathWithTortuga.addAll(pathAfterTortuga);
                pathLengthWithTortuga = pathWithTortuga.size() - 1;
            }
        }
        if (Math.min(pathLengthWithoutTortuga, pathLengthWithTortuga) >= 80) {
            backtrackingWriter.println("Lose");
            backtrackingWriter.close();
            return;
        }
        backtrackingWriter.println("Win");
        if (pathLengthWithoutTortuga <= pathLengthWithTortuga) {
            backtrackingWriter.println(pathLengthWithoutTortuga);
            displayResult(pathWithoutTortuga, map, backtrackingWriter);
        } else {
            backtrackingWriter.println(pathLengthWithTortuga);
            displayResult(pathWithTortuga, map, backtrackingWriter);
        }
    }

    private Stack<Node> traverse(Set<Node> visited, Node node, Map map, Node dest, boolean tortuga) {
        if (node == dest) {
            Stack<Node> t = new Stack<>();
            t.push(node);
            minPathLengthArray[node.x][node.y] = visited.size();
            return t;
        }
        if (node.rock) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (tortuga && node.kraken) {
            map.disableKraken();
        }
        if (node.enemy) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (visited.size() < minPathLengthArray[node.x][node.y]) {
            minPathLengthArray[node.x][node.y] = visited.size();
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
        for (int x = Math.max(0, node.x - 1); x <= Math.min(8, node.x + 1); x++) {
            for (int y = Math.max(0, node.y - 1); y <= Math.min(8, node.y + 1); y++) {
                if (!curVisited.contains(map.getNode(x, y))) {
                    Stack<Node> t = traverse(curVisited, map.getNode(x, y), map, dest, tortuga);
                    int l = t.size();
                    if (l < min) {
                        min = l;
                        minPath.clear();
                        minPath.addAll(t);
                        if (l == Math.max(Math.abs(node.x - dest.x), Math.abs(node.y - dest.y))) {
                            x = 9;
                            break;
                        }
                    }
                }
            }
        }
        if (tortuga && node.kraken)
            map.activateKraken();
        minPath.push(node);
        return minPath;
    }

    private void displayResult(Stack<Node> path, Map map, PrintWriter writer) {
        Stack<Node> t = new Stack<>();
        t.addAll(path);
        while (!t.isEmpty()) {
            Node e = t.pop();
            writer.printf("[%d,%d] ", e.x, e.y);
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
//    private int traverse(Set<Node> visited, Node node, Map map, Node dest, boolean tortuga) {
//        if (node == dest) {
//            return 0;
//        }
//        if (node.rock) {
//            return Integer.MAX_VALUE;
//        }
//        if (tortuga && node.kraken) {
//            map.disableKraken();
//        }
//        if (node.enemy) {
//            return Integer.MAX_VALUE;
//        }
//        visited.add(node);
//        int min = Integer.MAX_VALUE;
//        int minX = 9, minY = 9;
//        for (int x = Math.max(0, node.x - 1); x <= Math.min(8, node.x + 1); x++) {
//            for (int y = Math.max(0, node.y - 1); y <= Math.min(8, node.y + 1); y++) {
//                if (!visited.contains(map.getNode(x, y))) {
//                    int t = traverse(visited, map.getNode(x, y), map, dest, tortuga);
//                    if (t < min) {
//                        min = t;
//                        minX = x;
//                        minY = y;
//                    }
//
//                }
//            }
//        }
//        if (minX < 9) pathBacktracking.push(map.getNode(minX, minY));
//        return min;
//    }

    public void aStar(Map map, PrintWriter aStarWriter) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        Node initialNode = map.getNode(0, 0);
        Node finalNode = map.getChest();
        Node middleNode = map.getTortuga();
        int hvdCost = 1;

    }
}
