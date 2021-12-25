package advent.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Point {

    public Integer row;
    public Integer col;

    public Point(Integer row, Integer col) {
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
