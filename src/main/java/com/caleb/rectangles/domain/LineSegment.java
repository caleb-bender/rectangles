package com.caleb.rectangles.domain;

import java.util.Optional;

public record LineSegment(OrthogonalAxis axis, double constant, ParallelAxisBounds bounds) {

    /**
     * Gets the intersection of the current line segment with another if it exists
     * @param other
     * @return the point of intersection or empty
     */
    public Optional<Vector2> intersectionWith(LineSegment other) {
        if (this.axis() == other.axis()) return Optional.empty();
        if (
                other.constant >= this.bounds.lower() && other.constant <= this.bounds().upper() &&
                this.constant >= other.bounds.lower() && this.constant <= other.bounds().upper()
        ) {
            double x = this.axis() == OrthogonalAxis.X ? this.constant : other.constant();
            double y = this.axis() == OrthogonalAxis.Y ? this.constant : other.constant();
            return Optional.of(new Vector2(x, y));
        }
        return Optional.empty();
    }

    public boolean lowerOrUpperBoundaryIsBetween(LineSegment other) {
        return (bounds.lower >= other.bounds.lower &&
                bounds.lower <= other.bounds.upper) ||
                (bounds.upper >= other.bounds.lower &&
                bounds.upper <= other.bounds.upper);
    }

    public boolean lowerAndUpperBoundaryIsBetween(LineSegment other) {
        return bounds.lower >= other.bounds.lower &&
                bounds.lower <= other.bounds.upper &&
                bounds.upper >= other.bounds.lower &&
                bounds.upper <= other.bounds.upper;
    }

    public enum OrthogonalAxis {
        X,
        Y
    }
    public record ParallelAxisBounds(double lower, double upper) {
        public double difference() { return upper - lower; }
    }
}
