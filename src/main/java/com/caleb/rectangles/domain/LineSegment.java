package com.caleb.rectangles.domain;

import java.util.Optional;

public record LineSegment(OrthogonalAxis axis, double continuousValue, ParallelAxisBounds bounds) {

    /**
     * Gets the intersection of the current line segment with another if it exists
     * @param other
     * @return the point of intersection or empty
     */
    public Optional<Vector2> intersectionWith(LineSegment other) {
        if (this.axis() == other.axis()) return Optional.empty();
        if (
                other.continuousValue >= this.bounds.lower() && other.continuousValue <= this.bounds().upper() &&
                this.continuousValue >= other.bounds.lower() && this.continuousValue <= other.bounds().upper()
        ) {
            double x = this.axis() == OrthogonalAxis.X ? this.continuousValue : other.continuousValue();
            double y = this.axis() == OrthogonalAxis.Y ? this.continuousValue : other.continuousValue();
            return Optional.of(new Vector2(x, y));
        }
        return Optional.empty();
    }

    public enum OrthogonalAxis {
        X,
        Y
    }
    public record ParallelAxisBounds(double lower, double upper) {}
}
