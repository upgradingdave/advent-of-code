package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.ArrayList;
import java.util.List;


public class DayX {

    public static void main(String[] args) {
        final String DAY = "dayX";

        DayX solution = new DayX();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        //System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        //System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        //System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseLine(String line, Context context) {
        String[] row = line.trim().split("\\s+");

    }

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();

                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);

        return -1;
    }

    private static class Context {

        List<String> lines;

        public Context() {
            lines = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "lines=" + lines +
                    '}';
        }
    }
}
