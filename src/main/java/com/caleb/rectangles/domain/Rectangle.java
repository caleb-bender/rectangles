package com.caleb.rectangles.domain;

import java.util.HashMap;

public record Rectangle(Vector2 topLeft, Size size) {

    public static final int TOP_SEGMENT = 0;
    public static final int RIGHT_SEGMENT = 1;
    public static final int BOTTOM_SEGMENT = 2;
    public static final int LEFT_SEGMENT = 3;

    /**
     * Creates a Rectangle object
     * @param topLeft The coordinates of the top left corner
     * @param size The size expanding from the top left (width, height)
     */
    public Rectangle {
        var invariantViolations = new HashMap<String, String[]>();
        if (size.width() <= 0)
            invariantViolations.put("size.width", new String[] { "The width must be greater than zero"});
        if (size.height() <= 0)
            invariantViolations.put("size.height", new String[] { "The height must be greater than zero"});
        if (!invariantViolations.isEmpty()) throw new InvalidRectangleException(invariantViolations);
    }

    /**
     * Returns all segments of rectangle in the order top, right, bottom, left
     * @return segments
     */
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

    /**
     * Checks if the current rectangle contains another (inclusive)
     * @param rectangle
     * @return true if rectangle is within bounds (inclusive) and false if not
     */
    public boolean contains(Rectangle rectangle) {
        var otherSegments = rectangle.segments();

        var topIsContained = isWithinVerticalBounds(otherSegments[TOP_SEGMENT]);
        var rightIsContained = isWithinHorizontalBounds(otherSegments[RIGHT_SEGMENT]);
        var bottomIsContained = isWithinVerticalBounds(otherSegments[BOTTOM_SEGMENT]);
        var leftIsContained = isWithinHorizontalBounds(otherSegments[LEFT_SEGMENT]);

        return topIsContained && rightIsContained && bottomIsContained && leftIsContained;
    }

    boolean isWithinHorizontalBounds(LineSegment otherSegment) {
        var thisSegments = segments();
        return otherSegment.constant() >= thisSegments[Rectangle.LEFT_SEGMENT].constant()
                && otherSegment.constant() <= thisSegments[Rectangle.RIGHT_SEGMENT].constant();
    }

    boolean isWithinVerticalBounds(LineSegment otherSegment) {
        var thisSegments = segments();
        return otherSegment.constant() >= thisSegments[Rectangle.BOTTOM_SEGMENT].constant()
                && otherSegment.constant() <= thisSegments[Rectangle.TOP_SEGMENT].constant();
    }
}
