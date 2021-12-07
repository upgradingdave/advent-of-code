package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class Day6 {

    public static void main(String[] args) {
        final String DAY = "day6";

        Day6 day4 = new Day6();
        System.out.println("SAMPLE 1: " + day4.part1(DAY+"_sample.txt", 80));
        System.out.println("PART 1: " + day4.part1(DAY+"_input.txt", 80));
        System.out.println("SAMPLE 2: " + day4.part1(DAY+"_sample.txt", 256));
        System.out.println("PART 2: " + day4.part1(DAY+"_input.txt", 256));
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
                    Integer nextFish = Integer.parseInt(row[i]);
                    BigInteger totalFish = context.fish.get(nextFish);
                    if(totalFish == null) {
                        totalFish = new BigInteger("0");
                    }
                    totalFish = totalFish.add(new BigInteger("1"));
                    context.fish.put(nextFish, totalFish);
                }
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public BigInteger part1(String fileName, int maxGen) {
        Context context = parseContext(fileName);
        int gen = 0;
        while(gen < maxGen) {
            //System.out.println(context);
            context.next();
            gen++;
        }

        return context.getTotal();
    }

    private static class Context {

        Map<Integer, BigInteger> fish;

        public Context() {
            fish = new HashMap<>();
        }

        public void next() {

            Map<Integer, BigInteger> fishPrime = new HashMap<>();

            for(Integer f : fish.keySet()) {

                BigInteger fcount = fish.get(f);

                if(f == 0) {
                    // reset all these fish to 6
                    BigInteger fPrimeCount = fishPrime.get(6);
                    if(fPrimeCount == null) {
                        fPrimeCount = new BigInteger("0");
                    }
                    fPrimeCount = fPrimeCount.add(fcount);
                    fishPrime.put(6, fPrimeCount);

                    // all these fish produce 8's
                    fPrimeCount = fishPrime.get(8);
                    if(fPrimeCount == null) {
                        fPrimeCount = new BigInteger("0");
                    }
                    fPrimeCount = fPrimeCount.add(fcount);
                    fishPrime.put(8, fPrimeCount);
                } else {
                    BigInteger fPrimeCount = fishPrime.get(f-1);
                    if(fPrimeCount == null) {
                        fPrimeCount = new BigInteger("0");
                    }
                    fPrimeCount = fPrimeCount.add(fcount);
                    fishPrime.put(f-1, fPrimeCount);
                }
            }

            this.fish = fishPrime;
        }

        public BigInteger getTotal() {
            BigInteger count = new BigInteger("0");
            for(Integer i : fish.keySet()) {
                BigInteger v = fish.get(i);
                count = count.add(v);
            }
            return count;
        }

        @Override
        public String toString() {
            return "Context{" +
                    "fish=" + fish +
                    '}';
        }
    }
}
