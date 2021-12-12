package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;
import advent.support.Point;

import java.util.*;


public class Day11 {

    public static void main(String[] args) {
        final String DAY = "day11";

        Day11 solution = new Day11();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        //System.out.println("SAMPLE 2: " + solution.part1(DAY+"_sample2.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp = new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Map<Integer, Integer> row = parseIntegerMap(line);
                context.octoMap.grid.put(getLineNumber(), row);
                setLineNumber(getLineNumber()+1);
                setContext(context);
            }
        };

        fileManager.processLines(fileName, lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        for(int i=0; i<100; i++) {
            context.octoMap.doStep();
        }
        return context.octoMap.totalFlashes;
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);
        for(int i=0; i<1000; i++) {
            context.octoMap.doStep();
            if(context.octoMap.allFlash()) {
                return i+1;
            }
        }
        return -1;
    }

    private static class OctoMap {
        Map<Integer, Map<Integer, Integer>> grid;
        int step;
        int totalFlashes;

        public OctoMap() {
            grid = new HashMap<>();
            step = 0;
            totalFlashes = 0;
        }

        public boolean allFlash() {
            for(Integer row: grid.keySet()) {
                Map<Integer, Integer> r = grid.get(row);
                for(Integer col : r.keySet()) {
                    if(grid.get(row).get(col) != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Point neighbor(int row, int col) {
            Map<Integer, Integer> r = grid.get(row);
            Integer v = r == null ? null : r.get(col);
            return v==null? null : new Point(row, col);
        }

        public Set<Point> findNeighbors(Point p) {

            Set<Point> neighbors = new HashSet<>();
            Point al = neighbor(p.row-1, p.col-1);
            if(al != null) neighbors.add(al);
            Point a = neighbor(p.row-1, p.col);
            if(a != null) neighbors.add(a);
            Point ar = neighbor(p.row-1, p.col+1);
            if(ar != null) neighbors.add(ar);
            Point l = neighbor(p.row, p.col-1);
            if(l != null) neighbors.add(l);
            Point r = neighbor(p.row, p.col+1);
            if(r != null) neighbors.add(r);
            Point bl = neighbor(p.row+1, p.col-1);
            if(bl != null) neighbors.add(bl);
            Point b = neighbor(p.row+1, p.col);
            if(b != null) neighbors.add(b);
            Point br = neighbor(p.row+1, p.col+1);
            if(br != null) neighbors.add(br);

            return neighbors;

        }

        public void update(Set<Point> points, Set<Point> alreadyFlashed) {
            // update everything by 1
            for(Point p : points) {
                Integer v = grid.get(p.row).get(p.col);
                if(!alreadyFlashed.contains(p)) {
                    grid.get(p.row).put(p.col, v + 1);
                }
            }

            // handle flashes
            for(Point p : points) {
                Integer v = grid.get(p.row).get(p.col);
                if(v > 9) {
                    totalFlashes = totalFlashes + 1;
                    alreadyFlashed.add(p);
                    grid.get(p.row).put(p.col, 0);

                    Set<Point> neighbors = findNeighbors(p);
                    update(neighbors, alreadyFlashed);
                }
            }
        }

        public void doStep() {

            //System.out.println(this);

            // update all points
            Set<Point> points = new HashSet<>();
            for(Integer row: grid.keySet()) {
                Map<Integer, Integer> r = grid.get(row);
                for(Integer col : r.keySet()) {
                    points.add(new Point(row, col));
                }
            }
            update(points, new HashSet<Point>());
            step += 1;
        }

        @Override
        public String toString() {

            String gridStr = "";
            for(Integer row : grid.keySet()) {
                Map<Integer, Integer> r = grid.get(row);
                for(Integer col : r.keySet()) {
                    gridStr += grid.get(row).get(col);
                }
                gridStr += "\n";
            }

            return "OctoMap{" +
                    "step=" + step +
                    ", totalFlashes=" + totalFlashes + ",\n" +
                    gridStr +
                    '}';
        }
    }

    private static class Context {

        OctoMap octoMap;

        public Context() {
            octoMap = new OctoMap();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "octoMap=" + octoMap +
                    '}';
        }
    }
}
