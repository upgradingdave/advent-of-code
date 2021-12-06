package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.*;


public class Day5 {

    public static void main(String[] args) {
        final String DAY = "day5";

        Day5 day4 = new Day5();
        System.out.println("SAMPLE 1: " + day4.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + day4.part1(DAY+"_input.txt"));
        System.out.println("SAMPLE 2: " + day4.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + day4.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseLine(String txtLine, Context context, boolean part2) {

        //System.out.println(context);

        String[] row = txtLine.trim().split(" -> ");
        String[] coord = row[0].split(",");
        Line line = new Line();
        line.c1.x = Integer.parseInt(coord[0]);
        line.c1.y = Integer.parseInt(coord[1]);

        coord = row[1].split(",");
        line.c2.x = Integer.parseInt(coord[0]);
        line.c2.y = Integer.parseInt(coord[1]);

        // is this a vertical line?
        if(line.c1.x == line.c2.x) {
            int x = line.c1.x;
            // which is larger point?
            int startY = line.c1.y;
            int endY = line.c2.y;
            if(line.c1.y > line.c2.y) {
                startY = line.c2.y;
                endY = line.c1.y;
            }
            for(int i=startY; i<=endY; i++) {
                Coordinate c = new Coordinate(x, i);
                context.addIntersection(c);
            }
        }
        // is this a horizontal line?
        else if(line.c1.y == line.c2.y) {
            int y = line.c1.y;
            // which is larger point?
            int startX = line.c1.x;
            int endX = line.c2.x;
            if(line.c1.x > line.c2.x) {
                startX = line.c2.x;
                endX = line.c1.x;
            }
            for(int i=startX; i<=endX; i++) {
                Coordinate c = new Coordinate(i, y);
                context.addIntersection(c);
            }
        } else {
            if(part2) {
                // diagonal, 45 degree lines
                if(line.c1.x < line.c2.x) {
                    if(line.c1.y < line.c2.y) {
                        for(int x=line.c1.x,y=line.c1.y; x<=line.c2.x && y<=line.c2.y; x++,y++) {
                            Coordinate c = new Coordinate(x, y);
                            context.addIntersection(c);
                        }
                    } else {
                        for(int x=line.c1.x,y=line.c1.y; x<=line.c2.x && y>=line.c2.y; x++,y--) {
                            Coordinate c = new Coordinate(x, y);
                            context.addIntersection(c);
                        }
                    }
                } else {
                    if(line.c1.y < line.c2.y) {
                        for(int x=line.c1.x,y=line.c1.y; x>=line.c2.x && y<=line.c2.y; x--,y++) {
                            Coordinate c = new Coordinate(x, y);
                            context.addIntersection(c);
                        }
                    } else {
                        for(int x=line.c1.x,y=line.c1.y; x>=line.c2.x && y>=line.c2.y; x--,y--) {
                            Coordinate c = new Coordinate(x, y);
                            context.addIntersection(c);
                        }
                    }
                }
            }
        }
    }

    private Context parseContext(String fileName, final boolean part2) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                parseLine(line, context, part2);
                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName, false);

        int sum = 0;
        for(Coordinate key : context.intersections.keySet()) {
            int total = context.intersections.get(key);
            if(total >= 2) {
                sum = sum+1;
            }
        }

        //System.out.println(context);
        return sum;
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName, true);

        int sum = 0;
        for(Coordinate key : context.intersections.keySet()) {
            int total = context.intersections.get(key);
            if(total >= 2) {
                sum = sum+1;
            }
        }

        return sum;
    }

    private static class Coordinate {
        int x;
        int y;

        public Coordinate() {}

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return '[' + "x=" + x + "," + "y=" + y + ']';
        }
    }
    private static class Line {
        Coordinate c1 = new Coordinate();
        Coordinate c2 = new Coordinate();

        public Line() {}

        @Override
        public String toString() {
            return  c1 + " -> " + c2;
        }
    }

    private static class Context {

        Map<Coordinate, Integer> intersections;

        public Context() {
            intersections = new HashMap<>();
        }

        public void addIntersection(Coordinate c) {
            Integer total = this.intersections.get(c);
            if(total == null) {
                total = 0;
            }
            this.intersections.put(c, total+1);
        }

        @Override
        public String toString() {
            String result = "";
            for(int i=0; i<10; i++) {
                result += "\n";
                for(int j=0; j<10; j++) {
                    Integer value = intersections.get(new Coordinate(j, i));
                    if(value == null) {
                        result += ".";
                    } else {
                        result += value;
                    }
                }
            }
            return "Context [" + result + ']';
        }
    }
}
