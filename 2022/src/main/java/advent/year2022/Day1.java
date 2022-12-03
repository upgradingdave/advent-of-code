package advent.year2022;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        Day1 day1 = new Day1();

        //System.out.println(day1.part1("day1_sample1.txt"));
        //System.out.println("PART 1: " + day1.part1("day1_input.txt"));

        //System.out.println(day1.part2("day1_sample1.txt"));
        System.out.println("PART 2: " + day1.part2("day1_input.txt"));
    }

    FileManager fileManager = new FileManager();

    public Integer part1(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                Context context = getContext();

                if(line.length()<=0) {
                    // add up calories
                    int total = 0;
                    for (Integer calories : context.getPreviousCalories()) {
                        total += calories;
                    }

                    if (context.getMax() == null || context.getMax() < total) {
                        context.setMax(total);
                    }

                    context.setPreviousCalories(new ArrayList<>());
                } else {
                    int calories = Integer.parseInt(line);
                    context.getPreviousCalories().add(calories);
                }

                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return lp.getContext().getMax();
    }

    public void tallyCalories(Context context) {
        int total = 0;
        for (Integer calories : context.getPreviousCalories()) {
            total += calories;
        }

        context.getTotalCalories().add(total);
        context.setPreviousCalories(new ArrayList<>());
    }

    public Integer part2(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                Context context = getContext();

                if(line.length()<=0) {
                    tallyCalories(context);
                } else {
                    int calories = Integer.parseInt(line);
                    context.getPreviousCalories().add(calories);
                }

                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        tallyCalories(lp.getContext());

        lp.getContext().getTotalCalories().sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });

        int total = 0;
        for(int i=0; i<3; i++) {
            total += lp.getContext().getTotalCalories().get(i);
        }
        return total;
    }

    private static class Context {
        Integer max = null;

        List<Integer> previousCalories = new ArrayList<>();

        List<Integer> totalCalories = new ArrayList<>();

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        public List<Integer> getPreviousCalories() {
            return previousCalories;
        }

        public void setPreviousCalories(List<Integer> previousCalories) {
            this.previousCalories = previousCalories;
        }

        public List<Integer> getTotalCalories() {
            return totalCalories;
        }

        public void setTotalCalories(List<Integer> totalCalories) {
            this.totalCalories = totalCalories;
        }
    }
}
