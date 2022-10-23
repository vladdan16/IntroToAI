/**
 * A class Node to store necessary parameters of node on map
 */
class Node {
    private final int x, y;
    private boolean davy, kraken, rock, chest, tortuga, enemy, jack;

    /**
     * A constructor for Node
     * @param x x coordinate of node
     * @param y y coordinate of node
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        davy = false;
        kraken = false;
        rock = false;
        chest = false;
        tortuga = false;
        enemy = false;
        jack = false;
    }

    /**
     * Getter for x
     * @return int x value
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     * @return int y value
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for tortuga
     * @return boolean tortuga value
     */
    public boolean isTortuga() {
        return tortuga;
    }

    /**
     * Getter for davy
     * @return boolean davy value
     */
    public boolean isDavy() {
        return davy;
    }

    /**
     * Getter for kraken
     * @return boolean kraken value
     */
    public boolean isKraken () {
        return kraken;
    }

    /**
     * Getter for rock
     * @return boolean rock value
     */
    public boolean isRock() {
        return rock;
    }

    /**
     * Getter for chest
     * @return boolean chest value
     */
    public boolean isChest() {
        return chest;
    }

    /**
     * Getter for enemy
     * @return boolean enemy value
     */
    public boolean isEnemy() {
        return enemy;
    }

    /**
     * Getter for jack
     * @return boolean jack value
     */
    public boolean isJack() {
        return jack;
    }

    /**
     * Setter for davy
     * @param davy boolean davy value
     */
    public void setDavy(boolean davy) {
        this.davy = davy;
    }

    /**
     * Setter for kraken
     * @param kraken boolean kraken
     */
    public void setKraken(boolean kraken) {
        this.kraken = kraken;
    }

    /**
     * Setter for rock
     * @param rock boolean rock value
     */
    public void setRock(boolean rock) {
        this.rock = rock;
    }

    /**
     * Setter for chest
     * @param chest boolean chest value
     */
    public void setChest(boolean chest) {
        this.chest = chest;
    }

    /**
     * Setter for tortuga
     * @param tortuga boolean tortuga value
     */
    public void setTortuga(boolean tortuga) {
        this.tortuga = tortuga;
    }

    /**
     * Setter for enemy
     * @param enemy boolean enemy value
     */
    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    /**
     * Setter for jack
     * @param jack boolean jack value
     */
    public void setJack(boolean jack) {
        this.jack = jack;
    }
}
