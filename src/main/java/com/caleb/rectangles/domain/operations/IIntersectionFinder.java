package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;

public interface IIntersectionFinder {
    /**
     * Finds all the intersections between rectangles
     * @param rectangleA
     * @param rectangleB
     * @return intersections
     */
    Vector2[] findAll(Rectangle rectangleA, Rectangle rectangleB);
}
