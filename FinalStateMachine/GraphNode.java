import java.util.ArrayList;

class GraphNode {
    public final String name;
    public final ArrayList<GraphNode> next, previous;
    public boolean visited;

    public GraphNode(String name) {
        this.name = name;
        next = new ArrayList<>();
        previous = new ArrayList<>();
        visited = false;
    }

    public void visit(boolean p) {
        if (!visited) {
            visited = true;
            if(p) { for (GraphNode s : previous) s.visit(true); }
            for (GraphNode s : next) s.visit(p);
        }
    }

}