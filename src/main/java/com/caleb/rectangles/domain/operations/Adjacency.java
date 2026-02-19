package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.LineSegment;

public record Adjacency(Types type, LineSegment segment) {
    public enum Types {
        Proper,
        Partial,
        SubLine
    }
}
