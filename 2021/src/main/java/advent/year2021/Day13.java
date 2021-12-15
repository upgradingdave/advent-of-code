package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;
import advent.support.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Day13 {

    public static void main(String[] args) {
        final String DAY = "day13";

        Day13 solution = new Day13();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        //System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private void parseLine(String line, Context context) {

        if(line.isEmpty()) {
            //blank line
        } else if(line.startsWith("fold")) {
            String s1 = line.substring(10);
            String[] s2 = s1.trim().split("=");
            Point p = null;
            if(s2[0].equals("y")) {
                Integer col = null;
                Integer row = Integer.parseInt(s2[1]);
                p = new Point(row, col);
            } else {
                Integer col = Integer.parseInt(s2[1]);
                Integer row = null;
                p = new Point(row, col);
            }
            context.folds.add(p);
        } else {
            String[] s1 = line.trim().split(",");
            Integer col = Integer.parseInt(s1[0]);
            Integer row = Integer.parseInt(s1[1]);
            Point p = new Point(row, col);
            context.points.add(p);
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

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);

        context.fold(context.folds.get(0));
        int answer = context.points.size();;
        //System.out.println(context);

        context.fold(context.folds.get(1));
        //System.out.println(context);

        return answer;
    }

    public Integer part2(String fileName) {
        Context context = parseContext(fileName);
        //System.out.println(context);

        for(Point p : context.folds) {
            context.fold(p);
        }

        System.out.println(context);
        //RGZLBHFP

        return -1;
    }

    private static class Context {

        Set<Point> points;
        List<Point> folds;

        public Context() {
            points = new HashSet<>();
            folds = new ArrayList<>();
        }

        public void fold(Point p) {
            if(p.row == null) {
                System.out.println("Fold about col: " + p.col);
                foldAboutCol(p.col);
            } else {
                System.out.println("Fold about row: " + p.row);
                foldAboutRow(p.row);
            }
        }

        public void foldAboutRow(Integer row) {
            Set<Point> newPoints = new HashSet<>();
            for(Point p : points) {
                if(p.row > row) {
                    Point newP = new Point(row - (p.row - row), p.col);
                    newPoints.add(newP);
                } else {
                    newPoints.add(p);
                }
            }
            points = newPoints;
        }

        public void foldAboutCol(Integer col) {
            Set<Point> newPoints = new HashSet<>();
            for(Point p : points) {
                if(p.col > col) {
                    Point newP = new Point(p.row, col - (p.col - col));
                    newPoints.add(newP);
                } else {
                    newPoints.add(p);
                }
            }
            points = newPoints;
        }

        @Override
        public String toString() {

            String result = "";
            for(int row=0; row<100; row++) {
                for(int col=0; col<100; col++) {
                    Point p = new Point(row, col);
                    if(points.contains(p)) {
                        result += "#";
                    } else {
                        result += ".";
                    }
                }
                result += "\n";
            }
            return result;
        }
    }
}
