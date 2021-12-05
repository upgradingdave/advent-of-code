package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Day3 {

    public static void main(String[] args) {
        final String DAY = "day3";

        Day3 sample = new Day3(5);
        System.out.println("SAMPLE 1: " + sample.part1(DAY+"_sample.txt"));

        Day3 input = new Day3(12);
        System.out.println("PART 1: " + input.part1(DAY+"_input.txt"));

        System.out.println("SAMPLE 2: " + sample.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + input.part2(DAY+"_input.txt"));

    }

    public Integer length;
    public Day3(Integer length) {
        this.length = length;
    }

    FileManager fileManager = new FileManager();

    private Command parseLine(String line) {
        Command command = new Command();
        command.setLine(line);
        return command;
    }

    public Integer part1(String fileName) {

        LineProcessor<Context> lp =  new LineProcessor<Context>(new Context(length)) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Command command = parseLine(line);

                for(int i=0; i<length; i++) {
                    context.getGammaTally().inc(command.getChar(i), i);
                }
                setContext(context);
                //System.out.println(getContext());
            }
        };

        fileManager.processLines(fileName,lp);

        Integer gamma = lp.getContext().getGamma();
        Integer epsilon = lp.getContext().getEpsilon();
        //System.out.println(gamma);
        //System.out.println(epsilon);

        return gamma * epsilon;
    }

    public void filterOxygenCandidates(Context context, int level) {

        // should we include 0's or 1's?
        int include = 0;
        if(context.getOxygenTally().getOnes()[level] >= context.getOxygenTally().getZeros()[level]) {
            // include 1's
            include = 1;
        }

        // filter the list and update tally
        List<String> oxygenCandidates = new ArrayList<>();
        for(String candidate : context.getOxygenCandidates()) {
            if(Integer.parseInt(String.valueOf(candidate.charAt(level))) == include) {
                oxygenCandidates.add(candidate);
                if(level+1 < length) {
                    context.getOxygenTally().inc(Integer.parseInt(String.valueOf(candidate.charAt(level + 1))), level + 1);
                }
            }
        }

        context.setOxygenCandidates(oxygenCandidates);

    }

    public void filterCo2Candidates(Context context, int level) {

        // should we include 0's or 1's?
        int include = 0;
        if(context.getCo2Tally().getOnes()[level] < context.getCo2Tally().getZeros()[level]) {
            // include 1's
            include = 1;
        }

        // filter the list and update tally
        List<String> candidates = new ArrayList<>();
        for(String candidate : context.getCo2Candidates()) {
            if(Integer.parseInt(String.valueOf(candidate.charAt(level))) == include) {
                candidates.add(candidate);
                if(level+1 < length) {
                    context.getCo2Tally().inc(Integer.parseInt(String.valueOf(candidate.charAt(level + 1))), level + 1);
                }
            }
        }

        context.setCo2Candidates(candidates);

    }

    public Integer part2(String fileName) {

        Context context = new Context(length);
        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {
            @Override
            public void processLine(String line) {
                Context context = getContext();
                Command command = parseLine(line);

                // everything is a candidate at first
                context.getOxygenCandidates().add(command.getLine());
                context.getCo2Candidates().add(command.getLine());

                // update the oxygen tally at index 0
                context.getOxygenTally().inc(command.getChar(0), 0);
                context.getCo2Tally().inc(command.getChar(0), 0);

            }
        };
        fileManager.processLines(fileName,lp);

        // now filter candidates based on results
        int level=0;
        while(context.getOxygenCandidates().size() > 1) {
            filterOxygenCandidates(context, level);
            level++;
        }

        level = 0;
        while(context.getCo2Candidates().size() > 1) {
            filterCo2Candidates(context, level);
            level++;
        }

        Integer oxygen = Integer.parseInt(context.getOxygenCandidates().get(0),2);
        Integer co2 = Integer.parseInt(context.getCo2Candidates().get(0),2);

        return oxygen * co2;
    }

    private static class Command {
        String line;

        public Integer getChar(int i) {
            return Integer.parseInt(String.valueOf(line.charAt(i)));
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getLine() {
            return line;
        }
    }

    private static class OnesZeros {
        Integer[] ones;
        Integer[] zeros;

        public OnesZeros(Integer length) {
            ones = new Integer[length];
            Arrays.fill(ones, 0);
            zeros = new Integer[length];
            Arrays.fill(zeros, 0);
        }

        public Integer[] getOnes() {
            return ones;
        }

        public Integer[] getZeros() {
            return zeros;
        }

        void inc(int value, int index) {
            if(value == 0) {
                zeros[index] += 1;
            } else {
                ones[index] += 1;
            }
        }
    }

    private static class Context {

        Integer length = 0;
        OnesZeros gammaTally;
        OnesZeros oxygenTally;
        OnesZeros co2Tally;
        List<String> oxygenCandidates = new ArrayList<>();
        List<String> co2Candidates = new ArrayList<>();

        public Context(Integer length) {
            this.length = length;
            gammaTally = new OnesZeros(length);
            oxygenTally = new OnesZeros(length);
            co2Tally = new OnesZeros(length);
        }

        public OnesZeros getGammaTally() {
            return gammaTally;
        }

        public OnesZeros getCo2Tally() {
            return co2Tally;
        }

        public OnesZeros getOxygenTally() {
            return oxygenTally;
        }

        public List<String> getOxygenCandidates() {
            return oxygenCandidates;
        }

        public void setOxygenCandidates(List<String> oxygenCandidates) {
            this.oxygenCandidates = oxygenCandidates;
        }

        public List<String> getCo2Candidates() {
            return co2Candidates;
        }

        public void setCo2Candidates(List<String> co2Candidates) {
            this.co2Candidates = co2Candidates;
        }

        public Integer getGamma() {
            String gammaStr = "";
            for(int i=0; i<length; i++) {
                if(gammaTally.getOnes()[i] > gammaTally.getZeros()[i]) {
                    gammaStr += 1;
                } else {
                    gammaStr += 0;
                }
            }

            //System.out.println("GAMMA String: " + gammaStr);
            return Integer.parseInt(gammaStr,2);
        }

        public Integer getEpsilon() {
            String epsilonStr = "";
            for(int i=0; i<length; i++) {
                if(gammaTally.getOnes()[i] < gammaTally.getZeros()[i]) {
                    epsilonStr += 1;
                } else {
                    epsilonStr += 0;
                }
            }

            //System.out.println("EPSILON String: " + epsilonStr);
            return Integer.parseInt(epsilonStr,2);
        }
    }
}
