package com.epam.university.java.core.task015;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.epam.university.java.core.task015.Line.getLinesIntersection;
import static com.epam.university.java.core.task015.PointImpl.orderPoints;

public class SquareImpl implements Square {

    private final Point first;
    private final Point second;

    public SquareImpl(Point first, Point second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Point getFirst() {
        return first;
    }

    @Override
    public Point getSecond() {
        return second;
    }

    @Override
    public void setFirst(Point first) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSecond(Point second) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Get collection of all four vertices of a square.
     * @param square square  definition with two opposite points
     * @return collection of vertices
     */
    public static List<Point> getVertices(Square square) {
        Point minPoint = square.getFirst();
        Point maxPoint = square.getSecond();
        if (minPoint.getX() >= maxPoint.getX()
                && minPoint.getY() >= maxPoint.getY()) {
            minPoint = square.getSecond();
            maxPoint = square.getFirst();
        }
        double x1 = 0.5 * (minPoint.getX() + maxPoint.getX() - minPoint.getY() + maxPoint.getY());
        double x2 = 0.5 * (minPoint.getX() + maxPoint.getX() + minPoint.getY() - maxPoint.getY());
        double y1 = 0.5 * (minPoint.getY() + maxPoint.getY() + minPoint.getX() - maxPoint.getX());
        double y2 = 0.5 * (minPoint.getY() + maxPoint.getY() - minPoint.getX() + maxPoint.getX());
        ArrayList<Point> result = new ArrayList<>(Arrays.asList(
                minPoint,
                maxPoint,
                new PointImpl(x1, y1),
                new PointImpl(x2, y2)
        ));
        orderPoints(result);
        return result;
    }

    /**
     * Get true if a square contains the point, false otherwise.
     * @param point point, defined with its coordinates
     * @param square square  definition with two opposite points
     * @return boolean value
     */
    public static boolean  containsPoint(Point point, Square square) {
        List<Point> vertices = getVertices(square);
        final Vector a = new Vector(vertices.get(0), point);
        final Vector b = new Vector(vertices.get(0), vertices.get(1));
        final Vector c = new Vector(vertices.get(0), vertices.get(3));
        final double ab = a.scalarProduct(b);
        final double ad = a.scalarProduct(c);
        boolean result = 0 <= ab && ab <= b.scalarProduct(b)
                && 0 <= ad && ad <= c.scalarProduct(c);
        return result;
    }

    /**
     * Get point of in which two squares intersect.
     * @param first first square
     * @param second second square
     * @return collection of intersection points
     */
    public static List<Point> getIntersectionPoints(Square first, Square second) {
        ArrayList<Point> verticesOfFirst = (ArrayList<Point>) getVertices(first);
        ArrayList<Point> verticesOfSecond = (ArrayList<Point>) getVertices(second);
        List<Optional<Point>> optionalPoints = new ArrayList<>();
        int k;
        int m = verticesOfFirst.size();
        int n = verticesOfSecond.size();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                k = i + 1 < m ? i + 1 : 0;
                Line line1 = new Line(verticesOfFirst.get(i), verticesOfFirst.get(k));
                k = j + 1 < n ? j + 1 : 0;
                Line line2 = new Line(verticesOfSecond.get(j), verticesOfSecond.get(k));
                optionalPoints.add(getLinesIntersection(line1, line2));
            }
        }
        ArrayList<Point> result = new ArrayList<>();
        result.addAll(
                optionalPoints.stream().distinct().filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(p -> containsPoint(p, first))
                        .filter(p -> containsPoint(p,second))
                        .collect(Collectors.toList()));

        result.addAll(verticesOfFirst.stream()
                        .filter(p -> containsPoint(p, second))
                        .collect(Collectors.toList()));

        result.addAll(verticesOfSecond.stream()
                        .filter(p -> containsPoint(p, first))
                        .collect(Collectors.toList()));
        orderPoints(result);
        return result;
    }
}
