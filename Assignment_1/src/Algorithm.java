import java.io.PrintWriter;

public abstract class Algorithm {
    protected final PrintWriter writer;
    protected final Map map;

    protected Algorithm(Map map, PrintWriter writer) {
        this.writer = writer;
        this.map = map;
    }

    public abstract String compute();
}
