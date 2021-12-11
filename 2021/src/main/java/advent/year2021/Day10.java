package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.math.BigInteger;
import java.util.*;


public class Day10 {

    public static void main(String[] args) {
        final String DAY = "day10";

        Day10 solution = new Day10();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseLinePart1(String line, Context context) {

        for(int i=0; i<line.length(); i++) {
            Character c = line.charAt(i);
            Character p = context.stack.peek();

            if(p == null) {
                context.stack.push(c);
            } else {
                if( c.equals(')') && !p.equals('(') ) {
                    context.total += 3;
                    return;
                } else if ( c.equals(']') && !p.equals('[') ) {
                    context.total += 57;
                    return;
                } else if ( c.equals('>') && !p.equals('<') ) {
                    context.total += 25137;
                    return;
                } else if ( c.equals('}') && !p.equals('{') ) {
                    context.total += 1197;
                    return;
                } else if( c.equals ('}') || c.equals(')') || c.equals('>') || c.equals(']')) {
                    context.stack.pop();
                } else {
                    context.stack.push(c);
                }
            }
        }

    }

    private Integer part1(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                context.stack = new ArrayDeque<>();
                parseLinePart1(line, context);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context.total;
    }

    private void parseLinePart2(String line, Context context) {

        for(int i=0; i<line.length(); i++) {
            Character c = line.charAt(i);
            Character p = context.stack.peek();

            if(p == null) {
                context.stack.push(c);
            } else {
                if( c.equals(')') && !p.equals('(') ) {
                    return;
                } else if ( c.equals(']') && !p.equals('[') ) {
                    return;
                } else if ( c.equals('>') && !p.equals('<') ) {
                    return;
                } else if ( c.equals('}') && !p.equals('{') ) {
                    return;
                } else if( c.equals ('}') || c.equals(')') || c.equals('>') || c.equals(']')) {
                    context.stack.pop();
                } else {
                    context.stack.push(c);
                }
            }
        }

        Character p = context.stack.peek();
        BigInteger autoCompleteScore = new BigInteger("0");
        while(p != null) {
            Character c = context.stack.pop();
            autoCompleteScore = autoCompleteScore.multiply(new BigInteger("5"));
            if( c.equals('(')) {
                autoCompleteScore = autoCompleteScore.add(new BigInteger(("1")));
            } else if ( c.equals('[') ) {
                autoCompleteScore = autoCompleteScore.add(new BigInteger("2"));
            } else if ( c.equals('<') ) {
                autoCompleteScore = autoCompleteScore.add(new BigInteger("4"));
            } else if ( c.equals('{') ) {
                autoCompleteScore = autoCompleteScore.add(new BigInteger("3"));
            }

            p = context.stack.peek();
        }
        context.autoCompleteScores.add(autoCompleteScore);
    }

    private BigInteger part2(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                context.stack = new ArrayDeque<>();
                parseLinePart2(line, context);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);

        // sort the scores
        Collections.sort(context.autoCompleteScores);
        System.out.println(context.autoCompleteScores);
        int middle = context.autoCompleteScores.size() / 2 ;

        return context.autoCompleteScores.get(middle);
    }

    //Context{autoCompleteScores=[-1614312383, -1182889562, -1180399601, -1160594038, -1134033313, -1094093025, -909206358, -906463355, -655150478, -648515872, -417466861, -147603812, -56883828, 1042443, 1082244, 2644913, 7129968, 17087408, 27888946, 69823045, 78774135, 161914843, 224195796, 229953117, 394050544, 441358701, 454995543, 457681745, 489122866, 615624163, 798177363, 896357713, 908836022, 955426407, 1151153948, 1159196237, 1160123458, 1170840690, 1215506504, 1287637895, 1405766817, 1599253714, 1609777796, 1693084197, 1749322578, 1805179911, 1855054859, 2031169791, 2131240082]}

    private static class Context {

        Deque<Character> stack;
        Integer total;
        List<BigInteger> autoCompleteScores;

        public Context() {
            stack  = new ArrayDeque<>();
            total = 0;
            autoCompleteScores = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "autoCompleteScores=" + autoCompleteScores +
                    '}';
        }
    }
}
