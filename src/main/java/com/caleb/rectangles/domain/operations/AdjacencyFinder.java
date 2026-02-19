package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.LineSegment;
import com.caleb.rectangles.domain.Rectangle;

import java.util.ArrayList;
import java.util.Optional;

public class AdjacencyFinder implements IAdjacencyFinder {

    public Adjacency[] findAll(Rectangle rect1, Rectangle rect2) {

        var adjacencyList = new ArrayList<Adjacency>();
        for (LineSegment segment1 : rect1.segments()) {
            for (LineSegment segment2 : rect2.segments()) {
                if (!segmentsShareOrthogonalAxisAndConstant(segment1, segment2)) continue;
                Optional<LineSegment> partialSegment = getPartialSegment(segment1, segment2);
                Optional<LineSegment> subLineSegment = getSubLineSegment(segment1, segment2);
                if (segment1.equals(segment2))
                    adjacencyList.add(adjacencyFrom(Adjacency.Types.Proper, segment1));
                else if (subLineSegment.isPresent())
                    adjacencyList.add(adjacencyFrom(Adjacency.Types.SubLine, subLineSegment.get()));
                else if (partialSegment.isPresent())
                    adjacencyList.add(adjacencyFrom(Adjacency.Types.Partial, partialSegment.get()));
            }
        }
        return adjacencyList.toArray(new Adjacency[0]);
    }

    private static Adjacency adjacencyFrom(Adjacency.Types type, LineSegment segment) {
        return new Adjacency(type, segment);
    }

    private boolean segmentsShareOrthogonalAxisAndConstant(LineSegment segment1, LineSegment segment2) {
        return segment1.axis() == segment2.axis() && segment1.constant() == segment2.constant();
    }

    private Optional<LineSegment> getSubLineSegment(LineSegment segment1, LineSegment segment2) {
        if (segment2.lowerAndUpperBoundaryIsBetween(segment1)) {
            return getLineSegment(segment1, segment2);
        }
        return Optional.empty();
    }

    private Optional<LineSegment> getPartialSegment(LineSegment segment1, LineSegment segment2) {
        if (segment1.lowerOrUpperBoundaryIsBetween(segment2)) {
            return getLineSegment(segment1, segment2);
        }
        return Optional.empty();
    }

    private static Optional<LineSegment> getLineSegment(LineSegment segment1, LineSegment segment2) {
        var newLower = Math.max(segment1.bounds().lower(), segment2.bounds().lower());
        var newUpper = Math.min(segment1.bounds().upper(), segment2.bounds().upper());
        var newSegment = new LineSegment(segment1.axis(), segment1.constant(),
          new LineSegment.ParallelAxisBounds(newLower, newUpper));
        var newSegmentIsAPoint = newSegment.bounds().difference() == 0.0;
        if (newSegmentIsAPoint) return Optional.empty();
        return Optional.of(newSegment);
    }
}
