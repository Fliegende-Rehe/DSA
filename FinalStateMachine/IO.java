import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class IO {
    public static class Input {
        private final ArrayList<String[]> data;

        public Input(ArrayList<String[]> data) {
            this.data = data;
        }

        public String[] getLine(int index) {
            return data.get(index);
        }
    }

    public static Input Read() {
        ArrayList<String[]> input = new ArrayList<>();
        try {
            BufferedReader buffer = new BufferedReader(new FileReader("fsa.txt"));
            for (int i = 0; i < 5; i++) {
                String[] line = Objects.requireNonNull(buffer).readLine().split("=");
                Check(line);
                input.add(line[1].substring(1, line[1].length() - 1).split(","));
            }
        } catch (Exception e) {
            Write("Error:\nE5: Input file is malformed");
        }
        return new Input(input);
    }

    private static void Check(String[] line) {
        if(charFrequency(line[1], '[') != 1 || charFrequency(line[1], ']') != 1)
            Write("Error:\nE5: Input file is malformed");
    }

    private static int charFrequency(String arr, char value) {
        int n = 0;
        for (int i = 0; i < arr.length(); i++) {
            if(arr.charAt(i) == value) n++;
        }
        return n;
    }

    public static void Write(String output) {
        System.out.print(output);
        FileWriter writer;
        try {
            writer = new FileWriter("result.txt", false);
            writer.write(output);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
