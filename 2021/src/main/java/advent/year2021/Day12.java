package advent.year2021;

import advent.support.FileManager;
import advent.support.LineProcessor;

import java.util.*;


public class Day12 {

    public static void main(String[] args) {
        final String DAY = "day12";

        Day12 solution = new Day12();
        System.out.println("SAMPLE 1: " + solution.part1(DAY+"_sample.txt"));
        System.out.println("SAMPLE 2: " + solution.part1(DAY+"_sample2.txt"));
        System.out.println("PART 1: " + solution.part1(DAY+"_input.txt"));
        //System.out.println("SAMPLE 2: " + solution.part2(DAY+"_sample.txt"));
        //System.out.println("PART 2: " + solution.part2(DAY+"_input.txt"));
    }

    FileManager fileManager = new FileManager();

    private Context parseContext(String fileName) {

        Context context = new Context();

        LineProcessor<Context> lp =  new LineProcessor<Context>(context) {

            @Override
            public void processLine(String line) {
                Context context = getContext();
                String[] vs = line.split("-");

                Vertex one = new Vertex(vs[0]);
                Vertex two = new Vertex(vs[1]);

                List<Vertex> neighbors = new ArrayList<>();
                neighbors.add(two);
                context.g.addVertex(one, neighbors);

                neighbors = new ArrayList<>();
                neighbors.add(one);
                context.g.addVertex(two, neighbors);

                setContext(context);
            }
        };

        fileManager.processLines(fileName,lp);
        return context;
    }

    public Integer part1(String fileName) {
        Context context = parseContext(fileName);
        List<List<Vertex>> paths = context.g.findPaths();
        return paths.size();
    }

    private static class Graph {
        Map<Vertex, Set<Vertex>> vertices;

        public Graph() {
            vertices = new HashMap<>();
        }

        public void addVertex(Vertex v, List<Vertex> ns) {
            Set<Vertex> neighbors = vertices.get(v);
            if(neighbors == null) {
                neighbors = new HashSet<>();
            }
            neighbors.addAll(ns);
            vertices.put(v, neighbors);
        }

        public void findPaths(Vertex v, List<List<Vertex>> paths, List<Vertex> path) {

            if(v.label.equals("end")) {
                // we found a full path
                path.add(v);
                paths.add(path);

                //System.out.println("Found Path");
                //System.out.println(path);
                return;
            }

            if(v.label.equals("start")) {
                // no reason to ever go back to the start, this is not a valid path
                return;
            }

            if(v.label.toLowerCase().equals(v.label) && path.contains(v)) {
                Map<Vertex, Integer> counts = new HashMap<>();
                for(Vertex currVertex : path) {
                    if(currVertex.label.toLowerCase().equals(currVertex.label)) {
                        Integer count = counts.get(currVertex);
                        if (count == null) {
                            count = 0;
                        }
                        count = count + 1;

                        if (count > 1) {
                            return;
                        } else {
                            counts.put(currVertex, count);
                        }
                    }
                }
            }

            for(Vertex n : vertices.get(v)) {
                List<Vertex> newPath = new ArrayList<>(path);
                newPath.add(v);
                findPaths(n, paths, newPath);
            }
        }

        public List<List<Vertex>> findPaths() {
            Vertex start = new Vertex("start");
            Set<Vertex> neighbors = vertices.get(start);
            List<List<Vertex>> paths = new ArrayList<>();

            for(Vertex v : neighbors) {
                List<Vertex> newPath = new ArrayList<>();
                newPath.add(start);
                findPaths(v, paths, newPath);
            }
            return paths;
        }

    }

    private static class Vertex {
        String label;

        public Vertex(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(label, vertex.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static class Context {

        Graph g;

        public Context() {
            g = new Graph();
        }

        @Override
        public String toString() {
            return "Context{" +
                    "graph=" + g +
                    '}';
        }
    }
}
