package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;

import java.util.Optional;

public interface IAdjacencyFinder {
    /**
     * Returns all the unique adjacency relationships between two rectangles if there are any
     * @param rect1
     * @param rect2
     */
    Adjacency[] findAll(Rectangle rect1, Rectangle rect2);
}
