import java.io.PrintWriter;
import java.util.*;

/**
 * A class for AStar algorithm that extends Algorithm
 */
class AStar extends Algorithm{
    private final AStarNode[][] myMap;
    private final PriorityQueue<AStarNode> queue;
    private final Set<AStarNode> closedSet;
    private AStarNode initialNode;
    private AStarNode dest;
    private boolean tortuga;

    /**
     * A public constructor for AStar algorithm
     * @param map Map instance for algorithm
     * @param writer Writer instance for output
     */
    public AStar(Map map, PrintWriter writer) {
        super(map, writer);
        queue = new PriorityQueue<>();
        closedSet = new HashSet<>();
        myMap = new AStarNode[9][];
        tortuga = false;
        for (int i = 0; i < 9; i++) {
            myMap[i] = new AStarNode[9];
            for (int j = 0; j < 9; j++) {
                myMap[i][j] = new AStarNode(map.getNode(i, j));
            }
        }
    }
    public String[] compute() {
        //map.displayMap();
        initialNode = myMap[map.getJack().getX()][map.getJack().getY()];
        initialNode.setG(0);
        dest = myMap[map.getChest().getX()][map.getChest().getY()];
        initialNode.calculateH(dest);
        queue.add(initialNode);
        List<AStarNode> pathWithoutTortuga = findPath();
        int pathLengthWithoutTortuga = pathWithoutTortuga.size() - 1;
        if (pathLengthWithoutTortuga == -1)
            pathLengthWithoutTortuga = 81;
        List<AStarNode> pathWithTortuga = new ArrayList<>();
        int pathLengthWithTortuga = 81;
        if (pathLengthWithoutTortuga > Math.max(Math.abs(map.getJack().getX() - map.getTortuga().getX()), Math.abs(map.getJack().getY() - map.getTortuga().getY())) + Math.max(Math.abs(map.getTortuga().getX() - map.getChest().getX()), Math.abs(map.getTortuga().getY() - map.getChest().getY()))) {
            clearMyMap();
            dest = myMap[map.getTortuga().getX()][map.getTortuga().getY()];
            initialNode.calculateH(dest);
            queue.clear();
            queue.add(initialNode);
            closedSet.clear();
            pathWithTortuga = findPath();
            pathLengthWithTortuga = pathWithTortuga.size() - 1;
            if (pathLengthWithTortuga == -1)
                pathLengthWithTortuga = 81;
            if (pathLengthWithTortuga < pathLengthWithoutTortuga) {
                clearMyMap();
                initialNode = myMap[map.getTortuga().getX()][map.getTortuga().getY()];
                dest = myMap[map.getChest().getX()][map.getChest().getY()];
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
            return new String[]{"L", "0"};
        }
        writer.println("Win");
        if (pathLengthWithoutTortuga <= pathLengthWithTortuga) {
            writer.println(pathLengthWithoutTortuga);
            displayResult(pathWithoutTortuga);
            return new String[]{"W", String.valueOf(pathLengthWithoutTortuga)};
        } else {
            writer.println(pathLengthWithTortuga);
            displayResult(pathWithTortuga);
            return new String[]{"W", String.valueOf(pathLengthWithTortuga)};
        }
    }

    private List<AStarNode> findPath() {
        if (initialNode.equals(dest)) {
            ArrayList<AStarNode> t = new ArrayList<>();
            t.add(initialNode);
            return t;
        }
        map.activateKraken();
        if (initialNode.getNode().isEnemy()) {
            return new ArrayList<>();
        }
        while (!queue.isEmpty()) {
            AStarNode curNode = queue.poll();
            if (curNode == dest) {
                return getPath(curNode);
            }
            else {
                boolean tt = isKrakenNear(curNode);
                if (tortuga && tt || curNode.isWasKraken()) {
                    map.disableKraken();
                    curNode.setWasKraken(true);
                }
                for (int x = Math.max(0, curNode.getNode().getX() - 1); x <= Math.min(8, curNode.getNode().getX() + 1); x++) {
                    for (int y = Math.max(0, curNode.getNode().getY() - 1); y <= Math.min(8, curNode.getNode().getY() + 1); y++) {
                        if (myMap[x][y].equals(curNode))
                            continue;
                        addChild(curNode, x, y, 1, null);
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
                        int x = curNode.getNode().getX() + offset[i][0];
                        int y = curNode.getNode().getY() + offset[i][1];
                        int xt = x - offset[i][0] / 2;
                        int yt = y - offset[i][1] / 2;
                        if (x < 0 || x > 8 || y < 0 || y > 8)
                            continue;
                        if (myMap[xt][yt].getNode().isEnemy())
                            continue;
                        addChild(curNode, x, y, 2, myMap[xt][yt]);
                    }
                }
                closedSet.add(curNode);
                if (curNode.isWasKraken()) {
                    map.activateKraken();
                }
            }
        }
        return new ArrayList<>();
    }

    private void addChild(AStarNode curNode, int x, int y, int cost, AStarNode pr) {
        if (!(myMap[x][y].getG() > curNode.getG() + 1))
            return;
        myMap[x][y].calculateH(dest);
        int f = myMap[x][y].getH() + curNode.getG() + cost;
        if (queue.contains(myMap[x][y]) && myMap[x][y].getF() < f)
            return;
        if (closedSet.contains(myMap[x][y]) && myMap[x][y].getF() < f)
            return;
        if (myMap[x][y].getNode().isKraken() && myMap[x][y].getNode().isEnemy() && tortuga) {
            map.disableKraken();
        }
        if (myMap[x][y].getNode().isEnemy() || myMap[x][y].getNode().isRock()) {
            return;
        }
        if (cost == 2) {
            if (pr.getF() > curNode.getF())
                pr.setData(curNode);
            pr.calculateH(dest);
            myMap[x][y].setData(pr);
        } else {
            myMap[x][y].setData(curNode);
        }
        myMap[x][y].setWasKraken(curNode.isWasKraken());
        queue.add(myMap[x][y]);
        //displayMap();
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

    private boolean isKrakenNear(AStarNode curNode) {
        boolean t = false;
        for (int i = Math.max(0, curNode.getNode().getX() - 1); i <= Math.min(8, curNode.getNode().getX() + 1); i++) {
            for (int j = Math.max(0, curNode.getNode().getY() - 1); j <= Math.min(8, curNode.getNode().getY() + 1); j++) {
                if (myMap[i][j].getNode().isKraken() && i != curNode.getNode().getX() && j != curNode.getNode().getY()) {
                    t = true;
                    break;
                }
            }
        }
        return t;
    }

    private void clearMyMap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                myMap[i][j] = new AStarNode(map.getNode(i, j));
            }
        }
    }

    private void displayResult(List<AStarNode> path) {
        for (AStarNode e : path) {
            writer.printf("[%d,%d] ", e.getNode().getX(), e.getNode().getY());
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
