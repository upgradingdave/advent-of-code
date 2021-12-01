package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.ArrayList;
import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        Day1 day1 = new Day1();

        //System.out.println(day1.part1("day1_sample1.txt"));
        System.out.println("PART 1: " + day1.part1("day1_input.txt"));

        //System.out.println(day1.part2("day1_sample1.txt"));
        System.out.println("PART 2: " + day1.part2("day1_input.txt"));


    }

    FileManager fileManager = new FileManager();

    public Integer part1(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                Context context = getContext();

                int depth = Integer.parseInt(line);
                if(context.getPreviousDepths().size() == 1 && context.getPreviousDepths().get(0) < depth) {
                    context.setTotal(context.getTotal() + 1);
                }
                List<Integer> previous = context.getPreviousDepths();
                previous.clear();
                previous.add(depth);
                context.setPreviousDepths(previous);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        return lp.getContext().getTotal();
    }

    public Integer part2(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context()) {

            @Override
            public void processLine(String line) {
                int depth = Integer.parseInt(line);
                Context context = getContext();
                List<Integer> previous = context.getPreviousDepths();
                previous.add(depth);

                if(previous.size() == 4) {

                    List<Integer> newPrevious = new ArrayList<>();

                    // compare
                    Integer sum1 = 0;
                    for(int i=0; i<3; i++) {
                        sum1 += previous.get(i);
                    }

                    Integer sum2 = 0;
                    for(int i=1; i<4; i++) {
                        sum2 += previous.get(i);
                        // add these to the new list of previous depths
                        newPrevious.add(previous.get(i));
                    }

                    if(sum2 > sum1) {
                        context.setTotal(context.getTotal() + 1);
                    }

                    context.setPreviousDepths(newPrevious);

                } else {
                    context.setPreviousDepths(previous);
                }

                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        return lp.getContext().getTotal();
    }


    private static class Context {
        Integer total = 0;

        List<Integer> previousDepths = new ArrayList<>();

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<Integer> getPreviousDepths() {
            return previousDepths;
        }

        public void setPreviousDepths(List<Integer> previousDepths) {
            this.previousDepths = previousDepths;
        }
    }
}
