package com.caleb.rectangles.domain;

import java.util.HashMap;

public record Rectangle(Vector2 topLeft, Size size) {

    /**
     * Creates a Rectangle object
     * @param topLeft The coordinates of the top left corner
     * @param size The size expanding from the top left (width, height)
     */
    public Rectangle {
        var invariantViolations = new HashMap<String, String[]>();
        if (size.width() <= 0)
            invariantViolations.put("size.width", new String[] { "The width must be greater than zero."});
        if (size.height() <= 0)
            invariantViolations.put("size.height", new String[] { "The height must be greater than zero."});
        if (!invariantViolations.isEmpty()) throw new InvalidRectangleException(invariantViolations);
    }

    public LineSegment[] segments() {
        var leftX = topLeft.x();
        var rightX = leftX + size.width();
        var topY = topLeft.y();
        var bottomY = topY - size.height();
        var top = new LineSegment(
                LineSegment.OrthogonalAxis.Y, topY,
                new LineSegment.ParallelAxisBounds(leftX, rightX)
        );
        var right = new LineSegment(
                LineSegment.OrthogonalAxis.X, rightX,
                new LineSegment.ParallelAxisBounds(bottomY, topY)
        );
        var bottom = new LineSegment(
                LineSegment.OrthogonalAxis.Y, bottomY,
                new LineSegment.ParallelAxisBounds(leftX, rightX)
        );
        var left = new LineSegment(
                LineSegment.OrthogonalAxis.X, leftX,
                new LineSegment.ParallelAxisBounds(bottomY, topY)
        );
        return new LineSegment[]{ top, right, bottom, left };
    }
}
