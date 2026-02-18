package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.LineSegment;
import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;

import java.util.HashSet;

public class IntersectionFinder {

    /**
     * Finds all the intersections between rectangles
     * @param rectangleA
     * @param rectangleB
     * @return intersections
     */
    public Vector2[] FindAll(Rectangle rectangleA, Rectangle rectangleB) {
        var intersections = new HashSet<Vector2>();

        for (LineSegment segmentA : rectangleA.segments()) {
            for (LineSegment segmentB : rectangleB.segments()) {
                var intersection = segmentA.intersectionWith(segmentB);
                if (intersection.isEmpty()) continue;
                intersections.add(intersection.get());
            }
        }
        return intersections.toArray(new Vector2[0]);
    }
}
