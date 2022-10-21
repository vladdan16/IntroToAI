import java.io.PrintWriter;
import java.util.*;

public class AStar {
    private final Map map;
    private final AStarNode[][] myMap;
    private final PrintWriter writer;
    private final PriorityQueue<AStarNode> queue;
    private final Set<AStarNode> closedSet;
    private AStarNode dest;
    private boolean tortuga;

    public AStar(Map map, PrintWriter writer) {
        queue = new PriorityQueue<>();
        closedSet = new HashSet<>();
        this.map = map;
        this.writer = writer;
        myMap = new AStarNode[9][];
        tortuga = false;
        for (int i = 0; i < 9; i++) {
            myMap[i] = new AStarNode[9];
            for (int j = 0; j < 9; j++) {
                myMap[i][j] = new AStarNode(map.getNode(i, j));
            }
        }
    }

    public void compute() {
        AStarNode initialNode = myMap[map.getJack().x][map.getJack().y];
        initialNode.setG(0);
        dest = myMap[map.getChest().x][map.getChest().y];
        initialNode.calculateH(dest);
        queue.add(initialNode);
        List<AStarNode> pathWithoutTortuga = findPath();
        int pathLengthWithoutTortuga = pathWithoutTortuga.size() - 1;
        if (pathLengthWithoutTortuga == -1)
            pathLengthWithoutTortuga = 81;
        List<AStarNode> pathWithTortuga = new ArrayList<>();
        int pathLengthWithTortuga = 81;
        if (pathLengthWithoutTortuga > Math.max(Math.abs(map.getJack().x - map.getTortuga().x), Math.abs(map.getJack().y - map.getTortuga().y)) + Math.max(Math.abs(map.getTortuga().x - map.getChest().x), Math.abs(map.getTortuga().y - map.getChest().y))) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    myMap[i][j] = new AStarNode(map.getNode(i, j));
                }
            }
            dest = myMap[map.getTortuga().x][map.getTortuga().y];
            initialNode.calculateH(dest);
            queue.clear();
            queue.add(initialNode);
            closedSet.clear();
            pathWithTortuga = findPath();
            pathLengthWithTortuga = pathWithTortuga.size() - 1;
            if (pathLengthWithTortuga == -1)
                pathLengthWithTortuga = 81;
            if (pathLengthWithTortuga < pathLengthWithoutTortuga) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        myMap[i][j] = new AStarNode(map.getNode(i, j));
                    }
                }
                initialNode = myMap[map.getTortuga().x][map.getTortuga().y];
                dest = myMap[map.getChest().x][map.getChest().y];
                initialNode.calculateH(dest);
                initialNode.setG(0);
                queue.clear();
                queue.add(initialNode);
                closedSet.clear();
                tortuga = true;
                List<AStarNode> t = findPath();
                pathWithTortuga.addAll(t.subList(1, t.size()));
                pathLengthWithTortuga = pathWithTortuga.size() - 1;
                if (pathLengthWithTortuga == -1)
                    pathLengthWithTortuga = 81;
            }
        }
        if (Math.min(pathLengthWithoutTortuga, pathLengthWithTortuga) > 80) {
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

    private List<AStarNode> findPath() {
        while (!queue.isEmpty()) {
            AStarNode curNode = queue.poll();
            if (curNode == dest) {
                return getPath(curNode);
            } else {
                for (int x = Math.max(0, curNode.getNode().x - 1); x <= Math.min(8, curNode.getNode().x + 1); x++) {
                    for (int y = Math.max(0, curNode.getNode().y - 1); y <= Math.min(8, curNode.getNode().y + 1); y++) {
                        if (myMap[x][y].equals(curNode))
                            continue;
                        if (!(myMap[x][y].getG() > curNode.getG() + 1))
                            continue;
                        if (queue.contains(myMap[x][y]) && myMap[x][y].getG() < curNode.getG() + 1)
                            continue;
                        if (closedSet.contains(myMap[x][y]) && myMap[x][y].getG() < curNode.getG() + 1)
                            continue;
                        if (myMap[x][y].getNode().kraken && tortuga) {
                            map.disableKraken();
                            myMap[x][y].setWasKraken(true);
                        }
                        if (myMap[x][y].getNode().enemy) {
                            continue;
                        }
                        myMap[x][y].setData(curNode);
                        myMap[x][y].calculateH(dest);
                        queue.add(myMap[x][y]);
                    }
                }
                closedSet.add(curNode);
                if (curNode.isWasKraken()) {
                    map.activateKraken();
                    curNode.setWasKraken(false);
                }
            }
        }
        return new ArrayList<>();
    }

    private List<AStarNode> getPath(AStarNode node) {
        List<AStarNode> path = new ArrayList<>();
        path.add(node);
        AStarNode parent;
        while ((parent = node.getParent()) != null) {
            path.add(0, parent);
            node = parent;
        }
        return path;
    }

    private void displayResult(List<AStarNode> path) {
        for (AStarNode e : path) {
            writer.printf("[%d,%d] ", e.getNode().x, e.getNode().y);
        }
        writer.println();
        writer.println("-------------------");
        writer.println("  0 1 2 3 4 5 6 7 8");
        for (int i = 0; i < 9; i++) {
            for (int j = -1; j < 9; j++) {
                if (j == -1) {
                    writer.printf("%d ", i);
                } else {
                    if (path.contains(myMap[i][j])) {
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
