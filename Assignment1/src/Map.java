import java.util.Random;

class Map {
    private final Node[][] map;
    private int perceptionType;
    private Node jack;
    private Node tortuga;
    private Node chest;
    private Node kraken;
    private int[][] a;

    public Map() {
        map = new Node[9][];
        for (int i = 0; i < 9; i++) {
            map[i] = new Node[9];
            for (int j = 0; j < 9; j++) {
                map[i][j] = new Node(i, j);
            }
        }
        perceptionType = 0;
    }
    public boolean setObjects(int[][] a) {
        try {
            this.a = a;
            map[a[0][0]][a[0][1]].jack = true;
            jack = map[a[0][0]][a[0][1]];
            map[a[1][0]][a[1][1]].davy = true;
            setEnemyPerceptionZone(a[1], 1);
            map[a[2][0]][a[2][1]].kraken = true;
            setEnemyPerceptionZone(a[2], 2);
            kraken = map[a[2][0]][a[2][1]];
            map[a[3][0]][a[3][1]].rock = true;
            map[a[3][0]][a[3][1]].enemy = true;
            map[a[4][0]][a[4][1]].chest = true;
            chest = map[a[4][0]][a[4][1]];
            map[a[5][0]][a[5][1]].tortuga = true;
            tortuga = map[a[5][0]][a[5][1]];
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException was caught!");
            return false;
        }
    }
    public void disableKraken() {
        kraken.enemy = false;
        for (int x = Math.max(0, kraken.x - 1); x <= Math.min(8, kraken.x + 1); x++) {
            for (int y = Math.max(0, kraken.y - 1); y <= Math.min(8, kraken.y + 1); y++) {
                if (x != kraken.x && y != kraken.y) {
                    continue;
                }
                map[x][y].enemy = false;
            }
        }
        setEnemyPerceptionZone(a[1], 1);
    }
    public void activateKraken() {
        setEnemyPerceptionZone(a[2], 2);
    }
    public void generateObjects() {
        int[] coordinates;
        boolean ok = false;
        map[0][0].jack = true;
        jack = map[0][0];
        while (!ok) { // generate coordinates for Davy
            coordinates = generateCoordinates();
            if (coordinates[0] != 0 || coordinates[1] != 0) {
                map[coordinates[0]][coordinates[1]].davy = true;
                setEnemyPerceptionZone(coordinates, 1);
                ok = true;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for kraken
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].davy) {
                map[coordinates[0]][coordinates[1]].kraken = true;
                kraken = map[coordinates[0]][coordinates[1]];
                setEnemyPerceptionZone(coordinates, 2);
                ok = true;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for rock
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].davy) {
                map[coordinates[0]][coordinates[1]].rock = true;
                map[coordinates[0]][coordinates[1]].enemy = true;
                ok = true;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for chest
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].enemy) {
                map[coordinates[0]][coordinates[1]].chest = true;
                ok = true;
                chest = map[coordinates[0]][coordinates[1]];
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for tortuga
            coordinates = generateCoordinates();
            if (!map[coordinates[0]][coordinates[1]].enemy && !map[coordinates[0]][coordinates[1]].chest) {
                map[coordinates[0]][coordinates[1]].tortuga = true;
                ok = true;
                tortuga = map[coordinates[0]][coordinates[1]];
            }
        }
    }
    private int[] generateCoordinates() {
        Random random = new Random();
        int[] t = new int[2];
        t[0] = random.nextInt(9);
        t[1] = random.nextInt(9);
        return t;
    }
    private void setEnemyPerceptionZone(int[] b, int type) {
        for (int x = Math.max(0, b[0] - 1); x <= Math.min(8, b[0] + 1); x++) {
            for (int y = Math.max(0, b[1] - 1); y <= Math.min(8, b[1] + 1); y++) {
                if (type == 2 && (x != b[0] && y != b[1])) {
                    continue;
                }
                map[x][y].enemy = true;
            }
        }
    }
    public void displayMap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j].davy) {
                    System.out.print("d ");
                } else if (map[i][j].jack) {
                    System.out.print("j ");
                } else if (map[i][j].rock) {
                    System.out.print("r ");
                } else if (map[i][j].kraken) {
                    System.out.print("k ");
                } else if (map[i][j].enemy) {
                    System.out.print("e ");
                } else if (map[i][j].tortuga) {
                    System.out.print("t ");
                } else if (map[i][j].chest) {
                    System.out.print("c ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    public Node getNode(int x, int y) {
        return map[x][y];
    }

    public Node getTortuga() {
        return tortuga;
    }

    public Node getChest() {
        return chest;
    }

    public Node getJack() {
        return jack;
    }

    public Node getKraken() {
        return kraken;
    }

    public void setPerceptionType(int perceptionType) {
        this.perceptionType = perceptionType;
    }
}