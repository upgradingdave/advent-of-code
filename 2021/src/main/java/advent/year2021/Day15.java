package advent.year2021;

import advent.support.FileManager;
import advent.support.Grid;
import advent.support.LineProcessor;
import advent.support.Point;

import java.util.*;


public class Day15 {

    public static void main(String[] args) {
        final String DAY = "day15";

        Day15 solution = new Day15();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        //System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        //System.out.println("PART 1 SAMPLE 2: " + solution.part1(DAY+"_sample2.txt"));
        //System.out.println("Part 2 SAMPLE 1: " + solution.part2(DAY+"_sample.txt"));
        //System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                context.grid.grid.add(new ArrayList<Integer>());
                for(int i=0; i<line.length(); i++) {
                    String n = line.charAt(i) + "";
                    List<Integer> row = context.grid.grid.get(getLineNumber());
                    row.add(Integer.parseInt(n));
                }
                setContext(context);
                setLineNumber(getLineNumber() + 1);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);
        Point source = new Point(0,0);
        Point target = new Point(context.grid.grid.size()-1, context.grid.grid.get(0).size()-1);
        return context.dijkstra(source, target);
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);

        // create a bigger grid
        int smallRowSize = context.grid.grid.size();
        int smallColSize = context.grid.grid.get(0).size();
        Grid<Integer> biggerGrid = new Grid<>();
        for(int row=0; row<smallRowSize * 5; row++) {
            biggerGrid.grid.add(new ArrayList<Integer>());
            for(int col=0; col<smallRowSize * 5; col++) {
                int col2 = col;
                int inc = 0;
                if(col >= smallColSize) {
                    col2 = col % smallColSize;
                    inc = inc + col / smallColSize;
                }
                int row2 = row;
                if(row >= smallRowSize) {
                    row2 = row % smallRowSize;
                    inc = inc + row / smallRowSize;
                }
                Integer v = context.grid.valueAt(new Point(row2, col2));

                v = v+inc > 9 ? 1 + ((v+inc) % 10) : v+inc;
                biggerGrid.grid.get(row).add(v);
            }
        }
        context.grid = biggerGrid;

        //System.out.println(context);
        Point source = new Point(0,0);
        Point target = new Point(context.grid.grid.size()-1, context.grid.grid.get(0).size()-1);
        return context.dijkstra(source, target);
    }

    private static class PointDist implements Comparable<PointDist>{
        Point p;
        Integer dist;

        public PointDist(Point p, Integer dist) {
            this.p = p;
            this.dist = dist;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointDist pointDist = (PointDist) o;
            return p.equals(pointDist.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p);
        }

        @Override
        public int compareTo(PointDist o) {
            if(o == null) return 1;
            if(o.dist == null && this.dist == null) return 0;
            if(o.dist == null) return -1;
            if(this.dist == null) return 1;
            return this.dist.compareTo(o.dist);
        }
    }

    private static class Context {

        Grid<Integer> grid;

        SortedMap<Point, PointDist> points;
        Map<Point, Integer> dist;
        //Map<Point, Integer> dist;
        //SortedSet<PointDist> sortedDist;
        Map<Point, Point> prev;

        public Context() {
            grid = new Grid<>();
        }

        private void calcNeighbor(Point u, Point v) {
            PointDist pd = new PointDist(v, null);
            if(v != null && points.contains(pd)) {
                if(dist.get(pd) == null) {
                    PointDist altPd = new PointDist(v, grid.valueAt(v));
                    dist.put(altPd, grid.valueAt(v));
                    points.add(altPd);
                    prev.put(v, u);
                } else {
                    int alt = dist.get(pd) + grid.valueAt(v);
                    if(dist.get(pd) == null || alt < dist.get(pd)) {
                        PointDist altPd = new PointDist(v, alt);
                        dist.put(altPd, alt);
                        points.add(altPd);
                        prev.put(v, u);
                    }
                }
            }
        }

        public Integer dijkstra(Point source, Point target) {
            points = new TreeSet<>();
            dist = new HashMap<>();
            prev = new HashMap<>();
            for(int row=0; row<grid.grid.size(); row++) {
                for(int col=0; col<grid.grid.get(row).size(); col++) {
                    Point p = new Point(row, col);
                    PointDist pd = new PointDist(p, null);
                    boolean result = points.add(pd);
                    assert(result);
                }
            }

            PointDist s = new PointDist(source, 0);
            points.add(s);
            //dist.put(source, 0);

            boolean stop = false;
            while(!stop) {
                Integer min = null;
                /*Point u = null;
                for(Point p : points) {
                    if(dist.get(p) != null && (min == null || dist.get(p) < min)) {
                        min = dist.get(p);
                        u = p;
                    }
                }*/

                //if(pd != null) {
                if(!points.isEmpty()) {
                    PointDist pd = points.first();
                    Point u = pd.p;
                    points.remove(pd);

                    if(u.equals(target)) {
                        break;
                    }

                    Point v = grid.bottomNeighbor(u);
                    calcNeighbor(u, v);
                    v = grid.topNeighbor(u);
                    calcNeighbor(u, v);
                    v = grid.leftNeighbor(u);
                    calcNeighbor(u, v);
                    v = grid.rightNeighbor(u);
                    calcNeighbor(u, v);

                    stop = points.isEmpty();
                } else {
                    stop = true;
                }
            }

            PointDist targetPd = new PointDist(target, null);
            return dist.get(targetPd);
        }

        @Override
        public String toString() {
            return "Context{" +
                    "grid=" + grid +
                    '}';
        }
    }
}
