import java.io.PrintWriter;
import java.util.*;

/**
 * Class for Backtracking algorithm that extends Algorithm
 */
class Backtracking extends Algorithm {
    private final int[][] minPathLengthArray;

    /**
     * A public constructor for Backtracking algorithm
     *
     * @param map    Map instance for algorithm
     * @param writer Writer instance for output
     */
    public Backtracking(Map map, PrintWriter writer) {
        super(map, writer);
        minPathLengthArray = new int[9][9];
        setMinPathLengthArray();
    }

    public String[] compute() {
        //map.displayMap();
        Set<Node> visited = new HashSet<>();
        Stack<Node> t = traverse(visited, map.getJack(), map.getChest(), false);
        ArrayList<Node> pathWithoutTortuga = new ArrayList<>(t);
        Collections.reverse(pathWithoutTortuga);
        int pathLengthWithoutTortuga = pathWithoutTortuga.size() - 1;
        int pathLengthWithTortuga = 81;
        Stack<Node> tt;
        ArrayList<Node> pathWithTortuga = new ArrayList<>();
        //displayArrayResult(pathWithoutTortuga);
        if (pathLengthWithoutTortuga > Math.max(Math.abs(map.getJack().getX() - map.getTortuga().getX()), Math.abs(map.getJack().getY() - map.getTortuga().getY())) + Math.max(Math.abs(map.getTortuga().getX() - map.getChest().getX()), Math.abs(map.getTortuga().getY() - map.getChest().getY()))) {
            setMinPathLengthArray();
            visited.clear();
            tt = traverse(visited, map.getJack(), map.getTortuga(), false);
            pathWithTortuga = new ArrayList<>(tt);
            Collections.reverse(pathWithTortuga);
            pathLengthWithTortuga = pathWithTortuga.size() - 1;
            if (pathLengthWithTortuga < pathLengthWithoutTortuga) {
                setMinPathLengthArray();
                visited.clear();
                Stack<Node> ttt = traverse(visited, map.getTortuga(), map.getChest(), true);
                ttt.pop();
                ArrayList<Node> pathAfterTortuga = new ArrayList<>(ttt);
                Collections.reverse(pathAfterTortuga);
                pathWithTortuga.addAll(pathAfterTortuga);
                pathLengthWithTortuga = pathWithTortuga.size() - 1;
            }
        }
        if (Math.min(pathLengthWithoutTortuga, pathLengthWithTortuga) >= 80) {
            writer.println("Lose");
            return new String[]{"L", "0"};
        }
        writer.println("Win");
        if (pathLengthWithoutTortuga <= pathLengthWithTortuga) {
            writer.println(pathLengthWithoutTortuga);
            displayArrayResult(pathWithoutTortuga);
            return new String[]{"W", String.valueOf(pathLengthWithoutTortuga)};
        } else {
            writer.println(pathLengthWithTortuga);
            displayArrayResult(pathWithTortuga);
            return new String[]{"W", String.valueOf(pathLengthWithTortuga)};
        }
    }

    private void setMinPathLengthArray() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                minPathLengthArray[i][j] = 81;
            }
        }
    }

    private Stack<Node> traverse(Set<Node> visited, Node curNode, Node dest, boolean tortuga) {
        if (curNode == dest) {
            Stack<Node> t = new Stack<>();
            t.push(curNode);
            minPathLengthArray[curNode.getX()][curNode.getY()] = visited.size();
            return t;
        }
        if (curNode.isRock()) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (tortuga && curNode.isKraken()) {
            map.disableKraken();
        }
        if (curNode.isEnemy()) {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        if (visited.size() < minPathLengthArray[curNode.getX()][curNode.getY()]) {
            minPathLengthArray[curNode.getX()][curNode.getY()] = visited.size();
        } else {
            Stack<Node> t = new Stack<>();
            for (int i = 0; i < 9 * 9; i++) {
                t.push(new Node(0, 0));
            }
            return t;
        }
        Set<Node> curVisited = new HashSet<>(visited);
        curVisited.add(curNode);

        int min = 9 * 9;
        Stack<Node> minPath = new Stack<>();
        for (int i = 0; i < 9 * 9; i++) {
            minPath.push(new Node(0, 0));
        }
        boolean flag = false;

        for (int x = Math.max(0, curNode.getX() - 1); x <= Math.min(8, curNode.getX() + 1); x++) {
            for (int y = Math.max(0, curNode.getY() - 1); y <= Math.min(8, curNode.getY() + 1); y++) {
                if (curVisited.contains(map.getNode(x, y)))
                    continue;
                if (tortuga && isKrakenNear(curNode)) {
                    map.disableKraken();
                    flag = true;
                }
                Stack<Node> t = traverse(curVisited, map.getNode(x, y), dest, tortuga);
                int l = t.size();
                if (l < min) {
                    min = l;
                    minPath.clear();
                    minPath.addAll(t);
                    if (l == Math.max(Math.abs(curNode.getX() - dest.getX()), Math.abs(curNode.getY() - dest.getY()))) {
                        x = 9;
                        break;
                    }
                }
            }
        }
        if (map.getPerceptionType() == 2) {
            int[][] offset = {
                    {0, 2},
                    {2, 0},
                    {0, -2},
                    {-2, 0},
            };
            for (int i = 0; i < 4; i++) {
                int x = curNode.getX() + offset[i][0];
                int y = curNode.getY() + offset[i][1];
                int xt = x - offset[i][0] / 2;
                int yt = y - offset[i][1] / 2;
                if (x < 0 || x > 8 || y < 0 || y > 8)
                    continue;
                if (map.getNode(xt, yt).isEnemy())
                    continue;
                curVisited.add(map.getNode(xt, yt));
                Stack<Node> t = traverse(curVisited, map.getNode(x, y), dest, tortuga);
                int l = t.size();
                if (l < min) {
                    min = l;
                    minPath.clear();
                    minPath.addAll(t);
                    minPath.add(map.getNode(xt, yt));
                    if (l == Math.max(Math.abs(curNode.getX() - dest.getX()), Math.abs(curNode.getY() - dest.getY()))) {
                        break;
                    }
                }
            }
        }
        if (flag)
            map.activateKraken();
        minPath.push(curNode);
        return minPath;
    }

    private boolean isKrakenNear(Node curNode) {
        boolean t = false;
        for (int i = Math.max(0, curNode.getX() - 1); i <= Math.min(8, curNode.getX() + 1); i++) {
            for (int j = Math.max(0, curNode.getY() - 1); j <= Math.min(8, curNode.getY() + 1); j++) {
                if (map.getNode(i, j).isKraken() && i != curNode.getX() && j != curNode.getY()) {
                    t = true;
                    break;
                }
            }
        }
        return t;
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

    private void displayArrayResult(ArrayList<Node> path) {
        for (Node e : path) {
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