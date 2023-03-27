import java.util.ArrayList;
import java.util.Collections;

public class WarningsHandler {
    public final String[] fin;
    public static String[] trans, alpha;
    public final GraphNode init;
    public static GraphNode[] states;

    public WarningsHandler(GraphFactory graph) {
        states = graph.nodes;
        fin = graph.fin;
        trans = graph.trans;
        alpha = graph.edges;
        init = graph.init;
    }

    public String detect() {
        StringBuilder output = new StringBuilder();

        if (fin[0].equals(""))
            output.append("W1: Accepting state is not defined\n");

        init.visit(false);
        for (GraphNode s : GraphFactory.nodes) {
            if (!s.visited) output.append("W2: Some states are not reachable from the initial state\n");
        }

        if(isNondeterministic())
            output.append("W3: FSA is nondeterministic\n");

        if (output.length() != 0) output.insert(0, "Warning:\n");

        if(isComplete()) output.insert(0, "FSA is complete\n");
        else output.insert(0, "FSA is incomplete\n");

        return output.toString();
    }

    private static boolean isNondeterministic() {
        for(GraphNode s : states) {
            ArrayList<String> links = new ArrayList<>();
            for (String t : trans) {
                if (s.name.equals(t.split(">")[0]))
                    links.add(t.split(">")[1]);
            }
            for (int i = 0; i < links.size(); i++) {
                if (Collections.frequency(links, links.get(i)) > 1)
                    return true;
            }
        }
        return false;
    }

    private static boolean isComplete() {
        return states.length * alpha.length <= trans.length;
    }
}
