public class AStarNode implements Comparable<AStarNode>{
    private final Node node;
    private int f;
    private int g;
    private int h;
    private AStarNode parent;
    private boolean wasKraken;

    public AStarNode(Node node) {
        this.node = node;
        g = 81;
        h = 81;
        f = 81;
    }

    public void calculateH(AStarNode dest) {
        h = Math.max(Math.abs(node.x - dest.getNode().x), Math.abs(node.y - dest.getNode().y));
        computeF();
    }

    public void setData(AStarNode parent) {
        setG(parent.g + 1);
        setParent(parent);
        computeF();
    }

    @Override
    public boolean equals(Object o) {
        AStarNode node = (AStarNode) o;
        return this.getNode().x == node.getNode().x && this.getNode().y == node.getNode().y;
    }

    public void setWasKraken(boolean kraken) {
        wasKraken = kraken;
    }

    public boolean isWasKraken() {
        return wasKraken;
    }

    public int getF() {
        return f;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
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
    public void setParent(AStarNode parent) {
        this.parent = parent;
    }
    public AStarNode getParent() {
        return parent;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public int compareTo(AStarNode o) {
        if (this.f < o.f) {
            return -1;
        } else if (this.f > o.f) {
            return 1;
        } else {
            return Integer.compare(o.h, this.h);
        }
    }
}
