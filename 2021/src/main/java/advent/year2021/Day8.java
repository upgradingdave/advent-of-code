package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.math.BigInteger;
import java.util.*;


public class Day8 {

    public static void main(String[] args) {
        final String DAY = "day8";

        Day8 solution = new Day8();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2a: " + solution.part2(DAY+"_sample2.txt"));
        System.out.println("SAMPLE 2b: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Signal parseLine(String line) {
        String[] sections = line.trim().split("\\|");
        String[] inputsArr = sections[0].trim().split("\\s+");
        String[] outputsArr = sections[1].trim().split("\\s+");

        Signal signal = new Signal();
        signal.inputs = Arrays.asList(inputsArr);
        signal.outputs = Arrays.asList(outputsArr);

        return signal;
    }

    public Context parseContext(String fileName) {
        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Signal signal = parseLine(line);
                context.signals.add(signal);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {

        Context context = new Context();
        final Integer[] total = {0};

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Signal signal = parseLine(line);

                for(String output : signal.outputs) {
                    if(output.length() == 2 || output.length() == 4 || output.length() == 3 || output.length() == 7) {
                        total[0] += 1;
                    }
                }

                context.signals.add(signal);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return total[0];
    }

    public BigInteger part2(String fileName) {

        Context context = parseContext(fileName);
        BigInteger sum = new BigInteger("0");

        for(Signal signal : context.signals) {

            signal.solve();

            String total = "";
            for(String output : signal.outputs) {
                SevenSegmentNumber number = new SevenSegmentNumber(output, signal.key);
                total += number.toInt();
            }

            sum = sum.add(new BigInteger(total));
        }
        return sum;
    }

    public static final String TOP = "T";
    public static final String LEFT_TOP = "LT";
    public static final String RIGHT_TOP = "RT";
    public static final String MIDDLE = "M";
    public static final String LEFT_BOTTOM = "LB";
    public static final String RIGHT_BOTTOM = "RB";
    public static final String BOTTOM = "B";

    public static final Integer[] ZERO = {1, 1, 1, 0, 1, 1, 1};
    public static final Integer[] ONE = {0, 0, 1, 0, 0, 1, 0};
    public static final Integer[] TWO = {1, 0, 1, 1, 1, 0, 1};
    public static final Integer[] THREE = {1, 0, 1, 1, 0, 1, 1};
    public static final Integer[] FOUR = {0, 1, 1, 1, 0, 1, 0};
    public static final Integer[] FIVE = {1, 1, 0, 1, 0, 1, 1};
    public static final Integer[] SIX = {1, 1, 0, 1, 1, 1, 1};
    public static final Integer[] SEVEN = {1, 0, 1, 0, 0, 1, 0};
    public static final Integer[] EIGHT = {1, 1, 1, 1, 1, 1, 1};
    public static final Integer[] NINE = {1, 1, 1, 1, 0, 1, 1};

    private static class SevenSegmentNumber {
        //                    T LT  RT M  LB RB B
        Integer[] segments = {0, 0, 0, 0, 0, 0, 0};
        public SevenSegmentNumber(int t, int lt, int rt, int m, int lb, int rb, int b) {
            segments[0] = t;
            segments[1] = lt;
            segments[2] = rt;
            segments[3] = m;
            segments[4] = lb;
            segments[5] = rb;
            segments[6] = b;
        }

        public SevenSegmentNumber(String characters, Map<Character, String> key) {
            for(int i=0; i<characters.length(); i++) {
                String place = key.get(characters.charAt(i));
                if(place.equals(TOP)) {
                    segments[0] = 1;
                } else if (place.equals(LEFT_TOP)) {
                    segments[1] = 1;
                } else if (place.equals(RIGHT_TOP)) {
                    segments[2] = 1;
                } else if (place.equals(MIDDLE)) {
                    segments[3] = 1;
                } else if (place.equals(LEFT_BOTTOM)) {
                    segments[4] = 1;
                } else if (place.equals(RIGHT_BOTTOM)) {
                    segments[5] = 1;
                } else if (place.equals(BOTTOM)) {
                    segments[6] = 1;
                }
            }
        }

        public Integer toInt() {
            if(Arrays.equals(this.segments, ZERO)) {
                return 0;
            } else if(Arrays.equals(this.segments, ONE)) {
                return 1;
            } else if(Arrays.equals(this.segments, TWO)) {
                return 2;
            } else if(Arrays.equals(this.segments, THREE)) {
                return 3;
            } else if(Arrays.equals(this.segments, FOUR)) {
                return 4;
            } else if(Arrays.equals(this.segments, FIVE)) {
                return 5;
            } else if(Arrays.equals(this.segments, SIX)) {
                return 6;
            } else if(Arrays.equals(this.segments, SEVEN)) {
                return 7;
            } else if(Arrays.equals(this.segments, EIGHT)) {
                return 8;
            } else if(Arrays.equals(this.segments, NINE)) {
                return 9;
            } else {
                throw new IllegalStateException("Unable to convert to number");
            }
        }
    }

    private static class Signal {
        List<String> inputs;
        List<String> outputs;
        Map<String, Character> config;
        Map<Integer, List<String>> inputIndex;
        Map<Character, String> key;

        public Signal() {
            inputs = new ArrayList<>();
            outputs = new ArrayList<>();
            config = new HashMap<>();
            key = new HashMap<>();
        }

        // index inputs by length
        public Map<Integer, List<String>> getInputIndex() {

            if(inputIndex == null) {
                inputIndex = new HashMap<>();
                for (String input : inputs) {
                    List<String> result = inputIndex.get(input.length());
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(input);
                    inputIndex.put(input.length(), result);
                }
            }

            return inputIndex;
        }

        public String plus(String input1, String input2) {
            Set<Character> allChars = new HashSet<>();
            for(int i=0; i<input1.length(); i++) {
                allChars.add(input1.charAt(i));
            }
            for(int i=0; i<input2.length(); i++) {
                allChars.add(input2.charAt(i));
            }

            String result = "";
            for(Character c : allChars) {
                result += c;
            }
            return result;
        }

        public String minus(String input1, String input2) {

            Set<Character> allChars = new HashSet<>();
            for(int i=0; i<input1.length(); i++) {
                allChars.add(input1.charAt(i));
            }
            for(int i=0; i<input2.length(); i++) {
                allChars.remove(input2.charAt(i));
            }

            String result = "";
            for(Character c : allChars) {
                result += c;
            }
            return result;
        }

        public void solve() {
            solveTop();

            // now we have config, just reverse that to create key!
            for(String k : config.keySet()) {
                Character c = config.get(k);
                key.put(c, k);
            }
        }

        // the character that is only in 7 but not in 1 is the top
        public void solveTop() {
            String input7 = getInputIndex().get(3).get(0);
            String input1 = getInputIndex().get(2).get(0);
            config.put(TOP, minus(input7, input1).charAt(0));
            solveBottom();
        }

        public String find3() {
            // find 3
            // out of 2, 3, and 5, 3 is the only one that has both chars found in 1
            List<String> inputs = getInputIndex().get(5);
            String input1 = getInputIndex().get(2).get(0);
            for(int i=0; i<3; i++) {
                String input = inputs.get(i);
                if(input.contains(""+input1.charAt(0)) && input.contains(""+input1.charAt(1))) {
                    return input;
                }
            }
            throw new IllegalStateException("Can't find 3");
        }

        public void solveBottom() {
            String input3 = find3();
            String input4 = getInputIndex().get(4).get(0);

            String bottom = minus(input3, input4);
            bottom = minus(bottom, ""+config.get(TOP));
            assert(bottom.length() == 1);
            config.put(BOTTOM, bottom.charAt(0));
            solveLeftBottom();
        }

        public String find9() {
            String input3 = find3();
            String input4 = getInputIndex().get(4).get(0);
            return plus(input3, input4);
        }

        public void solveLeftBottom() {
            String input8 = getInputIndex().get(7).get(0);

            String result = minus(input8, find9());
            assert(result.length() == 1);
            config.put(LEFT_BOTTOM, result.charAt(0));
            solveLeftTop();
        }

        public void solveLeftTop() {
            String leftTop = minus(find9(), find3());
            assert(leftTop.length() == 1);
            config.put(LEFT_TOP, leftTop.charAt(0));
            solveMiddle();
        }

        public void solveMiddle() {
            String input4 = getInputIndex().get(4).get(0);
            String input1 = getInputIndex().get(2).get(0);
            String result = minus(input4, config.get(LEFT_TOP)+"");
            result = minus(result, input1);
            assert(result.length() == 1);
            config.put(MIDDLE, result.charAt(0));
            solveBottomRight();
        }

        public String find6() {
            List<String> inputs = getInputIndex().get(6);
            String input9 = find9();

            List<String> noNine = new ArrayList<>();
            for(int i=0; i<3; i++) {
                String input = inputs.get(i);
                int sameCount = 0;
                for(int j=0; j<input9.length(); j++) {
                    if(input.contains(""+input9.charAt(j))) {
                        sameCount += 1;
                    }
                }
                if(sameCount != 6) {
                    noNine.add(input);
                }
            }

            assert(noNine.size() == 2);

            // the number that has middle is 6
            for(String input : noNine) {
                if(input.contains(config.get(MIDDLE) + "")) {
                    return input;
                }
            }
            throw new IllegalStateException("Can't find 6");
        }

        public void solveBottomRight() {
            String input6 = find6();
            String result = minus(input6, ""+config.get(TOP));
            result = minus(result, ""+config.get(LEFT_TOP));
            result = minus(result, ""+config.get(MIDDLE));
            result = minus(result, ""+config.get(LEFT_BOTTOM));
            result = minus(result, ""+config.get(BOTTOM));
            assert(result.length() == 1);
            config.put(RIGHT_BOTTOM, result.charAt(0));
            solveTopRight();
        }

        public void solveTopRight() {
            String input8 = inputIndex.get(7).get(0);
            String result = minus(input8, find6());
            assert(result.length() == 1);
            config.put(RIGHT_TOP, result.charAt(0));
        }

        @Override
        public String toString() {
            return "Signal{" +
                    "inputs=" + inputs +
                    ", outputs=" + outputs +
                    '}';
        }
    }

    private static class Context {

        List<Signal> signals = new ArrayList<>();

        @Override
        public String toString() {
            return "Context{" +
                    "signals=" + signals +
                    '}';
        }
    }
}
