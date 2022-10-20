public class Node implements Comparable{
    int x, y;
    boolean davy, kraken, rock, chest, tortuga, enemy, jack;

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

    @Override
    public int compareTo(Object o) {
        Node t = (Node) o;
        return 0;
    }
}
