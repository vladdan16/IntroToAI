import java.util.Random;

/**
 * Class for map that store objects
 */
class Map {
    private final Node[][] map;
    private int perceptionType;
    private Node jack;
    private Node tortuga;
    private Node chest;
    private Node kraken;
    private int[][] a;

    /**
     * A constructor that creates necessary fields for map
     */
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

    /**
     * Method to set Objects on map from given array
     * @param a 2-d array with coordinates of objects
     * @return boolean value: true if operation was ended successful, false otherwise
     */
    public boolean setObjects(int[][] a) {
        try {
            this.a = a;
            map[a[0][0]][a[0][1]].setJack(true);
            jack = map[a[0][0]][a[0][1]];
            map[a[1][0]][a[1][1]].setDavy(true);
            setEnemyPerceptionZone(a[1], 1);
            map[a[2][0]][a[2][1]].setKraken(true);
            setEnemyPerceptionZone(a[2], 2);
            kraken = map[a[2][0]][a[2][1]];
            map[a[3][0]][a[3][1]].setRock(true);
            map[a[3][0]][a[3][1]].setEnemy(true);
            map[a[4][0]][a[4][1]].setChest(true);
            chest = map[a[4][0]][a[4][1]];
            map[a[5][0]][a[5][1]].setTortuga(true);
            tortuga = map[a[5][0]][a[5][1]];
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException was caught!");
            return false;
        }
    }

    /**
     * A method to generate objects randomly
     */
    public void generateObjects() {
        a = new int[6][2];
        a[0][0] = 0; a[0][1] = 0;
        int[] coordinates;
        boolean ok = false;
        map[0][0].setJack(true);
        jack = map[0][0];
        while (!ok) { // generate coordinates for Davy
            coordinates = generateCoordinates();
            if (coordinates[0] != 0 || coordinates[1] != 0) {
                map[coordinates[0]][coordinates[1]].setDavy(true);
                setEnemyPerceptionZone(coordinates, 1);
                ok = true;
                a[1] = coordinates;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for kraken
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].isDavy()) {
                map[coordinates[0]][coordinates[1]].setKraken(true);
                kraken = map[coordinates[0]][coordinates[1]];
                setEnemyPerceptionZone(coordinates, 2);
                ok = true;
                a[2] = coordinates;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for rock
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].isDavy()) {
                map[coordinates[0]][coordinates[1]].setRock(true);
                map[coordinates[0]][coordinates[1]].setEnemy(true);
                ok = true;
                a[3] = coordinates;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for chest
            coordinates = generateCoordinates();
            if ((coordinates[0] != 0 || coordinates[1] != 0) && !map[coordinates[0]][coordinates[1]].isEnemy()) {
                map[coordinates[0]][coordinates[1]].setChest(true);
                ok = true;
                chest = map[coordinates[0]][coordinates[1]];
                a[4] = coordinates;
            }
        }
        ok = false;
        while (!ok) { // generate coordinates for tortuga
            coordinates = generateCoordinates();
            if (!map[coordinates[0]][coordinates[1]].isEnemy() && !map[coordinates[0]][coordinates[1]].isChest()) {
                map[coordinates[0]][coordinates[1]].setTortuga(true);
                ok = true;
                tortuga = map[coordinates[0]][coordinates[1]];
                a[5] = coordinates;
            }
        }
    }

    /**
     * A method to disable kraken (remove its perception zone)
     */
    public void disableKraken() {
        kraken.setEnemy(false);
        for (int x = Math.max(0, kraken.getX() - 1); x <= Math.min(8, kraken.getX() + 1); x++) {
            for (int y = Math.max(0, kraken.getY() - 1); y <= Math.min(8, kraken.getY() + 1); y++) {
                if (x != kraken.getX() && y != kraken.getY()) {
                    continue;
                }
                map[x][y].setEnemy(false);
            }
        }
        setEnemyPerceptionZone(a[1], 1);
    }

    /**
     * A method to activate kraken and restore its perception zone
     */
    public void activateKraken() {
        setEnemyPerceptionZone(a[2], 2);
        map[a[2][0]][a[2][1]].setEnemy(true);
    }

    /**
     * A method that displays current map to console
     */
    public void displayMap() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j].isDavy()) {
                    System.out.print("d ");
                } else if (map[i][j].isJack()) {
                    System.out.print("j ");
                } else if (map[i][j].isRock()) {
                    System.out.print("r ");
                } else if (map[i][j].isKraken()) {
                    System.out.print("k ");
                } else if (map[i][j].isEnemy()) {
                    System.out.print("e ");
                } else if (map[i][j].isTortuga()) {
                    System.out.print("t ");
                } else if (map[i][j].isChest()) {
                    System.out.print("c ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    /**
     * A method to get Node by given coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return Node instance
     */
    public Node getNode(int x, int y) {
        return map[x][y];
    }

    /**
     * Getter for perception type
     * @return int perception type value
     */
    public int getPerceptionType() {
        return perceptionType;
    }

    /**
     * A method to get tortuga node instance
     * @return Node instance
     */
    public Node getTortuga() {
        return tortuga;
    }

    /**
     * A method to get chest node instance
     * @return Node instance
     */
    public Node getChest() {
        return chest;
    }

    /**
     * A method to get jack node instance
     * @return Node instance
     */
    public Node getJack() {
        return jack;
    }

    /**
     * A method to get kraken node instance
     * @return Node instance
     */
    public Node getKraken() {
        return kraken;
    }

    /**
     * Setter for perception type
     * @param perceptionType int value of perception type
     */
    public void setPerceptionType(int perceptionType) {
        this.perceptionType = perceptionType;
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
                map[x][y].setEnemy(true);
            }
        }
    }

}