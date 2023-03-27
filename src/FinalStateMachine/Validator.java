public class Validator {
    public static void main(String[] args) {
        IO.Input description = IO.Read();

        GraphFactory graph = new GraphFactory(description);

        WarningsHandler warnings = new WarningsHandler(graph);

        String validation_result = warnings.detect();

        IO.Write(validation_result);
    }
}
