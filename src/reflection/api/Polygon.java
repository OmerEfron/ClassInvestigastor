package reflection.api;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Polygon {

    private final int stam = 2;
    private Set<Point> points;

    public Polygon() {
        points = new HashSet<Point>();
    }

    public int getTotalPoints() {
        return points.size();
    }

    protected void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }
}

