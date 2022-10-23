/**
 * A class AStarNode to store necessary parameters of node for AStar algorithm
 */
public class AStarNode implements Comparable<AStarNode>{
    private final Node node;
    private int f;
    private int g;
    private int h;
    private AStarNode parent;
    private boolean wasKraken;

    /**
     * A public constructor for AStarNode that creates a node with g, h, f equal 81 (max values)
     * @param node Parameter that needs to create AStarNode from simple Node
     */
    public AStarNode(Node node) {
        this.node = node;
        g = 81;
        h = 81;
        f = 81;
    }

    /**
     * A method to calculate value of h (distance) from this node to given AStarNode and recompute f
     * @param dest AStarNode to which we need to calculate h
     */
    public void calculateH(AStarNode dest) {
        h = Math.max(Math.abs(node.getX() - dest.getNode().getX()), Math.abs(node.getY() - dest.getNode().getY()));
        computeF();
    }

    /**
     * A method to set data for this AStarNode
     * we set parent node to this node and value of g and then compute value of f
     * @param parent An AStarNode that we set to field parent
     */
    public void setData(AStarNode parent) {
        setG(parent.g + 1);
        setParent(parent);
        computeF();
    }

    /**
     * Overridden method to check if two AStarNode instances are equal
     * @param o Object that we need to compare with current instance
     * @return boolean value: true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        AStarNode node = (AStarNode) o;
        return this.getNode().getX() == node.getNode().getX() && this.getNode().getY() == node.getNode().getY();
    }

    /**
     * Method to mark node if we disabled kraken from here
     * @param kraken boolean value
     */
    public void setWasKraken(boolean kraken) {
        wasKraken = kraken;
    }

    /**
     * Method to check if we disabled kraken from this node
     * @return boolean value
     */
    public boolean isWasKraken() {
        return wasKraken;
    }

    /**
     * Getter for g value
     * @return int g value
     */
    public int getG() {
        return g;
    }

    /**
     * Getter for h value
     * @return int h value
     */
    public int getH() {
        return h;
    }

    /**
     * Getter for f value
     * @return int f value
     */
    public int getF() {
        return f;
    }

    /**
     * Setter for g value
     * We set value of g and recompute value of f
     * @param g int g value
     */
    public void setG(int g) {
        this.g = g;
        computeF();
    }

    /**
     * Method to get parent of current node
     * @return AStarNode parent instance
     */
    public AStarNode getParent() {
        return parent;
    }

    /**
     * Method to get initial node from which AStarNode instance was created
     * @return Node instance
     */
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
            //return Integer.compare(this.h, o.h);
            return Integer.compare(o.h, this.h);
        }
    }

    private void setParent(AStarNode parent) {
        this.parent = parent;
    }

    private void computeF() {
        f = g + h;
    }
}
