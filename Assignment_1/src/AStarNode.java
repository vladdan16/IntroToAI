public class AStarNode implements Comparable<AStarNode>{
    private Node node;
    private int f;
    private int g;
    private int h;

    public AStarNode(Node node) {
        this.node = node;
        g = 81;
        h = 81;
        computeF();
    }

    public void setG(int g) {
        this.g = g;
        computeF();
    }
    public void setH(int h) {
        this.h = h;
        computeF();
    }

    private void computeF() {
        f = g + h;
    }

    @Override
    public int compareTo(AStarNode o) {
        if (this.f < o.f) {
            return 1;
        } else if (this.f > o.f) {
            return -1;
        } else {
            return Integer.compare(o.h, this.h);
        }
    }
}
