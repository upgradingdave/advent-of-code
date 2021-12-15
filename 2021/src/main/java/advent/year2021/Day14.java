package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class Day14 {

    public static void main(String[] args) {
        final String DAY = "day14";

        Day14 solution = new Day14();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseLine(String line, Context context) {

        if(line.isEmpty()) {
            //blank line
        } else if(line.contains("->")) {
            String[] s1 = line.trim().split("->");
            context.rules.put(s1[0].trim(), s1[1].trim());
        } else {
            context.template = line;
        }
    }

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                parseLine(line, context);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public BigInteger part1(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);
        for(int i=0; i<10; i++) {
            context.applyRules();
            //System.out.println(context);
        }
        return context.updateStats();
    }

    public BigInteger part2(String fileName) {
        Context context = parseContext(fileName);
        context.maxLevel = 40;
        return context.doPairCount();
    }

    private static class Context {

        String template;
        Map<String, String> rules;
        Map<Character, BigInteger> stats;
        int maxLevel = 10;

        public Context() {
            rules = new HashMap<>();
            stats = new HashMap<>();
        }

        public void applyRules() {
            char c1 = template.charAt(0);
            String newTemplate = "" + c1;
            for(int i=1; i<template.length(); i++) {
                char c2 = template.charAt(i);
                String testStr = "" + c1 + c2;
                String rule = rules.get(testStr);
                if(rule != null) {
                    newTemplate += rule + c2;
                } else {
                    newTemplate += c2;
                }
                c1 = c2;
            }
            template = newTemplate;
        }

        Map<String, BigInteger> pairCounts = new HashMap<>();

        public BigInteger doPairCount() {
            char c1 = template.charAt(0);
            for(int i=1; i<template.length(); i++) {
                char c2 = template.charAt(i);
                String pair = "" + c1 + c2;
                BigInteger count = pairCounts.get(pair) == null ? new BigInteger("0") : pairCounts.get(pair);
                pairCounts.put(pair, count.add(new BigInteger("1")));
                c1 = c2;
            }

            for(int i=0; i<maxLevel; i++) {
                applyRules3();
            }

            for(String k: pairCounts.keySet()) {
                BigInteger t1 = pairCounts.get(k);
                if(!t1.equals(new BigInteger("0"))) {
                    BigInteger count = stats.get(k.charAt(0)) == null ? new BigInteger("0") : stats.get(k.charAt(0));
                    stats.put(k.charAt(0), count.add(t1));
                }
            }
            char lastChar = template.charAt(template.length()-1);
            stats.put(lastChar, stats.get(lastChar).add(new BigInteger("1")));

            return calculateStats();
        }

        public void applyRules3() {
            Map<String, BigInteger> newPairCounts = new HashMap<>();
            for(String pair : pairCounts.keySet()) {
                newPairCounts.put(pair, pairCounts.get(pair));
            }

            for (String pair : pairCounts.keySet()) {
                String v = rules.get(pair);
                if(v != null) {
                    // need to adjust counts
                    BigInteger orig = pairCounts.get(pair);

                    // old pair goes to 0
                    BigInteger existing = newPairCounts.get(pair) == null ? new BigInteger("0") : newPairCounts.get(pair);
                    newPairCounts.put(pair, existing.subtract(orig));

                    // update the 2 new pairs
                    String newPair1 = pair.charAt(0) + v;
                    existing = newPairCounts.get(newPair1) == null ? new BigInteger("0") : newPairCounts.get(newPair1);
                    newPairCounts.put(newPair1, existing.add(orig));

                    String newPair2 = v + pair.charAt(1);
                    existing = newPairCounts.get(newPair2) == null ? new BigInteger("0") : newPairCounts.get(newPair2);
                    newPairCounts.put(newPair2, existing.add(orig));
                }
            }
            pairCounts = newPairCounts;
        }

        /**
         * // This was a big fail! I thought maybe processing pairs as if I was traversing a tree would cut it into log n time
         *  // But that didn't work at all!
        public BigInteger applyRules2() {
            char c1 = template.charAt(0);
            stats.put(c1, new BigInteger("1"));
            for(int i=1; i<template.length(); i++) {
                char c2 = template.charAt(i);
                System.out.println(""+c1+c2);
                Map<Character, BigInteger> counts = applyRules(0, c1, c2);
                stats = mergeCounts(stats, counts);
                c1 = c2;
            }
            System.out.println(stats);
            return calculateStats();
        }

        public Map<Character, BigInteger> mergeCounts(Map<Character, BigInteger> counts1,
                                                      Map<Character, BigInteger> counts2) {
            Map<Character, BigInteger> counts = new HashMap<>();
            for(Character c : counts1.keySet()) {
                BigInteger t = counts1.get(c);
                counts.put(c, t);
            }
            for(Character c : counts2.keySet()) {
                BigInteger t = counts2.get(c);
                if(counts.get(c) != null) {
                    counts.put(c, t.add(counts.get(c)));
                } else {
                    counts.put(c, t);
                }
            }
            return counts;
        }

        public Map<Character, BigInteger> applyRules(int level, char c1, char c2) {

            Map<Character, BigInteger> counts = new HashMap<>();

            if(level < maxLevel) {

                String testStr = "" + c1 + c2;

                Map<Character, BigInteger> counts1 = new HashMap<>();
                Map<Character, BigInteger> counts2 = new HashMap<>();
                if (rules.get(testStr) != null) {
                    char v = rules.get(testStr).charAt(0);
                    counts1 = applyRules(level + 1, c1, v);
                    counts2 = applyRules(level + 1, v, c2);
                } else {
                    counts.put(c2, new BigInteger("1"));
                }

                counts = mergeCounts(counts, counts1);
                counts = mergeCounts(counts, counts2);

            } else {
                counts.put(c2, new BigInteger("1"));
            }

            return counts;
        } */

        public BigInteger updateStats() {
            for(int i=0; i<template.length(); i++) {
                Character c = template.charAt(i);
                BigInteger count = stats.get(c);
                if(count == null) {
                    count = new BigInteger("0");
                }
                stats.put(c, count.add(new BigInteger("1")));
            }

            return calculateStats();
        }

        public BigInteger calculateStats() {
            Character mostCommon = null;
            Character leastCommon = null;
            for(Character c : stats.keySet()) {
                if(mostCommon == null) {
                    mostCommon = c;
                }
                if(stats.get(c).compareTo(stats.get(mostCommon)) > 0) {
                    mostCommon = c;
                }
                if(leastCommon == null) {
                    leastCommon = c;
                }
                if(stats.get(c).compareTo(stats.get(leastCommon)) < 0) {
                    leastCommon = c;
                }
            }

            return stats.get(mostCommon).subtract(stats.get(leastCommon));
        }

        @Override
        public String toString() {
            return "Context{" +
                    "stats=" + stats + "\n" +
                    "template=" + template + "\n" +
                    "rules=" + rules +
                    '}';
        }
    }
}
