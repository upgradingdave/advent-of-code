package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Day7 {

    public static void main(String[] args) {
        final String DAY = "day7";

        Day7 day4 = new Day7();
        System.out.println("SAMPLE 1: " + day4.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + day4.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + day4.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + day4.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                String[] row = line.trim().split(",");
                for(int i=0; i<row.length; i++) {
                    int position = Integer.parseInt(row[i]);
                    if(context.min == null || position < context.min) {
                        context.min = position;
                    }
                    if(context.max == null || position > context.max) {
                        context.max = position;
                    }
                    context.positions.add(position);
                }
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);

        Integer result = null;
        for(int i=context.min; i < context.max; i++) {
            int fuel = context.calculateFuel(i);
            if(result == null || fuel < result) {
                result = fuel;
            }
        }

        return result;
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);

        Integer result = null;
        for(int i=context.min; i < context.max; i++) {
            int fuel = context.calculateFuel2(i);
            if(result == null || fuel < result) {
                result = fuel;
            }
        }

        return result;
    }

    private static class Context {

        List<Integer> positions;
        Integer min;
        Integer max;
        Map<Integer, Integer> fuelCosts;

        public Context() {
            positions = new ArrayList<>();
            fuelCosts = new HashMap<>();
        }

        public int calculateFuel(int position) {
            int result = 0;
            for(Integer pos : positions) {
                if(pos > position) {
                    result += pos - position;
                } else {
                    result += position - pos;
                }
            }
            return result;
        }

        public Integer getFuelCost(int distance) {
            if(distance == 0) {
                return 0;
            }

            Integer fuelCost = this.fuelCosts.get(distance);
            if(fuelCost == null) {
                fuelCost = distance + getFuelCost(distance-1);
                this.fuelCosts.put(distance, fuelCost);
            }
            return fuelCost;
        }

        public int calculateFuel2(int position) {
            int result = 0;
            for(Integer pos : positions) {
                int distance = position - pos;
                if(pos > position) {
                    distance = pos - position;
                }
                result += getFuelCost(distance);
            }
            return result;
        }

        @Override
        public String toString() {
            return "Context{" +
                    "positions=" + positions +
                    '}';
        }
    }
}
