import java.io.PrintWriter;

/**
 * An abstract class for our algorithms: Backtracking and AStar
 */
public abstract class Algorithm {

    /**
     * A protected field writer to print output of algorithm
     */
    protected final PrintWriter writer;

    /**
     * A protected field map where our Map for algorithm is stored
     */
    protected final Map map;

    /**
     * A protected constructor for Algorithms
     * @param map Map instance for algorithm
     * @param writer Writer instance for output
     */
    protected Algorithm(Map map, PrintWriter writer) {
        this.writer = writer;
        this.map = map;
    }

    /**
     * A public method to start performing algorithm
     * @return String value: L (Lose) or W (Win)
     */
    public abstract String compute();
}
