package advent.support;

import java.util.*;

public class Grid<T> {

    public List<List<T>> grid;

    public Grid() {
        this.grid = new ArrayList<>();
    }

    public T valueAt(Point p) {
        List<T> r = grid.get(p.row);
        return r == null ? null : r.get(p.col);
    }

    public Point neighbor(int row, int col) {
        if(row < 0 || row >= grid.size()) {
            return null;
        } else {
            List<T> r = grid.get(row);
            if(col < 0 || col >= grid.get(row).size()) {
                return null;
            } else {
                T v = r == null ? null : r.get(col);
                return v==null? null : new Point(row, col);
            }
        }
    }

    public Point topNeighbor(Point p) {
        return neighbor(p.row-1, p.col) == null ? null : new Point(p.row-1, p.col);
    }

    public Point leftNeighbor(Point p) {
        return neighbor(p.row, p.col-1) == null ? null : new Point(p.row, p.col-1);
    }

    public Point rightNeighbor(Point p) {
        return neighbor(p.row, p.col+1) == null ? null : new Point(p.row, p.col+1);
    }

    public Point bottomNeighbor(Point p) {
        return neighbor(p.row+1, p.col) == null ? null : new Point(p.row+1, p.col);
    }

    @Override
    public String toString() {
        return "Grid{" +
                "grid=" + grid +
                '}';
    }
}
