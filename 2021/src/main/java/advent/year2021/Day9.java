package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.*;


public class Day9 {

    public static void main(String[] args) {
        final String DAY = "day9";

        Day9 solution = new Day9();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Map<Integer, Integer> parseLine(String line) {
        Map<Integer, Integer> row = new HashMap<>();
        for(int i=0; i<line.length(); i++) {
            Character c = line.charAt(i);
            row.put(i, Integer.parseInt(""+c));
        }
        return row;
    }

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                Map<Integer, Integer> row = parseLine(line);
                context.heightMap.heightMap.put(getLineNumber(), row);
                setContext(context);
                setLineNumber(getLineNumber() + 1);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        return context.heightMap.sumLowPoints();
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);
        context.heightMap.buildBasins();
        return context.heightMap.basinsTotalSize();
    }

    private static class Point {
        Integer row;
        Integer col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Objects.equals(row, point.row) &&
                    Objects.equals(col, point.col);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + "," + col +')';
        }
    }

    public static class Basin {
        Set<Point> points;

        public Basin() {
            points = new HashSet<>();
        }
    }

    private static class HeightMap {
        Map<Integer, Map<Integer, Integer>> heightMap;
        List<Basin> basins;
        private Basin basin;

        public HeightMap() {
            heightMap = new HashMap<>();
            basins = new ArrayList<>();
        }

        public Integer above(int row, int col) {
            Map<Integer, Integer> r = heightMap.get(row-1);
            return r == null ? null : r.get(col);
        }

        public Integer below(int row, int col) {
            Map<Integer, Integer> r = heightMap.get(row+1);
            return r == null ? null : r.get(col);
        }

        public Integer left(int row, int col) {
            Map<Integer, Integer> r = heightMap.get(row);
            return r == null ? null : r.get(col-1);
        }

        public Integer right(int row, int col) {
            Map<Integer, Integer> r = heightMap.get(row);
            return r == null ? null : r.get(col+1);
        }

        public boolean isLowPoint(int row, int col) {
            Integer above = above(row, col);
            Integer below = below(row, col);
            Integer left = left(row, col);
            Integer right = right(row, col);
            int v = heightMap.get(row).get(col);
            return ((above == null || v < above)
                    && (below == null || v < below)
                    && (left == null || v < left)
                    && (right == null || v < right) );
        }

        public int sumLowPoints() {
            int total = 0;
            for(int row : heightMap.keySet()) {
                for(int col : heightMap.get(row).keySet()) {
                    if(isLowPoint(row, col)) {
                        total += (heightMap.get(row).get(col) + 1);
                    }
                }
            }
            return total;
        }

        public void buildBasin(Point p) {
            Integer above = above(p.row, p.col);
            if(above != null && above != 9) {
                Point newPoint = new Point(p.row-1, p.col);
                if(!basin.points.contains(newPoint)) {
                    basin.points.add(newPoint);
                    buildBasin(newPoint);
                }
            }

            Integer below = below(p.row, p.col);
            if(below != null && below != 9) {
                Point newPoint = new Point(p.row+1, p.col);
                if(!basin.points.contains(newPoint)) {
                    basin.points.add(newPoint);
                    buildBasin(newPoint);
                }
            }

            Integer left = left(p.row, p.col);
            if(left != null && left != 9) {
                Point newPoint = new Point(p.row, p.col-1);
                if(!basin.points.contains(newPoint)) {
                    basin.points.add(newPoint);
                    buildBasin(newPoint);
                }
            }

            Integer right = right(p.row, p.col);
            if(right != null && right != 9) {
                Point newPoint = new Point(p.row, p.col+1);
                if(!basin.points.contains(newPoint)) {
                    basin.points.add(newPoint);
                    buildBasin(newPoint);
                }
            }
        }

        public void buildBasins() {
            for(int row : heightMap.keySet()) {
                for(int col : heightMap.get(row).keySet()) {
                    Point p = new Point(row, col);
                    Integer v = heightMap.get(row).get(col);
                    if(v != 9 && !basinsContainPoint(p)) {
                        // start building a new basin
                        basin = new Basin();
                        basin.points.add(p);
                        buildBasin(p);
                        basins.add(basin);
                    }
                }
            }
        }

        public boolean basinsContainPoint(Point p) {
            for(Basin basin : basins) {
                if(basin.points.contains(p)) {
                    return true;
                }
            }
            return false;
        }

        public int basinsTotalSize() {

            List<Integer> sizes = new ArrayList<>();

            for(Basin basin: basins) {
                int s = basin.points.size();
                System.out.println("Basin Size:" + s);
                sizes.add(s);
            }

            Collections.sort(sizes, Collections.<Integer>reverseOrder());
            int total = 1;
            for(int i=0; i<3; i++) {
                System.out.println("Mulitply:" + sizes.get(i));
                total = total * sizes.get(i);
            }
            return total;
        }

    }
    private static class Context {

        HeightMap heightMap;

        public Context() {
            heightMap = new HeightMap();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "heightMap=" + heightMap +
                    '}';
        }
    }
}
