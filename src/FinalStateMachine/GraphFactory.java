import java.util.Objects;

public class GraphFactory {
    public final String[] fin, trans, edges;
    public final GraphNode init;
    public static GraphNode[] nodes;

    public GraphFactory(IO.Input input) {
        fin = input.getLine(3);
        trans = input.getLine(4);
        edges = input.getLine(1);

        nodes = new GraphNode[input.getLine(0).length];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = new GraphNode(input.getLine(0)[i]);
        for (String t : input.getLine(4)) {
            String[] tr = t.split(">");

            boolean flag = true;
            for (String a : input.getLine(1)) {
                if(a.equals(tr[1])) flag = false;
            }
            if(flag) IO.Write("Error:\nE3: A transition '" + tr[1] + "' is not represented in the alphabet");

            Objects.requireNonNull(getNode(tr[0])).next.add(getNode(tr[2]));
            Objects.requireNonNull(getNode(tr[2])).previous.add(getNode(tr[0]));
        }

        nodes[0].visit(true);
        for (GraphNode s : nodes) {
            if (!s.visited) IO.Write("Error:\nE2: Some states are disjoint");
        }
        for (GraphNode s : nodes) s.visited = false;

        if (input.getLine(2)[0].equals("")) IO.Write("Error:\nE4: Initial state is not defined");
        init = getNode(input.getLine(2)[0]);
    }

    private GraphNode getNode(String name) {
        for (GraphNode s : nodes) {
            if (s.name.equals(name)) return s;
        }
        IO.Write("Error:\nE1: A state '" + name + "' is not in the set of states");
        return null;
    }
}
