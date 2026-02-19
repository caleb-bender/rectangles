package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;

import java.util.Optional;

public interface IAdjacencyFinder {
    /**
     * Returns the adjacency relationship between two rectangles if there is one.
     * @param rect1
     * @param rect2
     */
    Optional<Adjacency> find(Rectangle rect1, Rectangle rect2);
}
