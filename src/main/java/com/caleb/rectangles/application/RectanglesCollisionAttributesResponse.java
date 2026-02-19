package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;
import com.caleb.rectangles.domain.operations.Adjacency;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Response containing collision attributes and rectangles, or errors.
 * @param errors
 * @param attributes
 * @param rectangles
 */
public record RectanglesCollisionAttributesResponse(
        Map<String, String[]> errors,
        RectangleCollisionAttributes attributes,
        Rectangles rectangles
) {

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public Optional<Vector2[]> intersections() {
        return attributes.intersections == null ? Optional.empty() :
                Optional.of(attributes.intersections);
    }

    public Optional<ContainmentInfo> containmentInfo() {
        return attributes.containmentInfo == null ? Optional.empty() :
                Optional.of(attributes.containmentInfo);
    }

    public Optional<Adjacency[]> adjacencyList() {
        return attributes.adjacencyList == null ? Optional.empty() :
                Optional.of(attributes.adjacencyList);
    }

    public Optional<Rectangle> rectangle1() {
        return rectangles.rectangle1 == null ? Optional.empty() :
                Optional.of(rectangles.rectangle1);
    }

    public Optional<Rectangle> rectangle2() {
        return rectangles.rectangle2 == null ? Optional.empty() :
                Optional.of(rectangles.rectangle2);
    }

    /**
     * Houses collision attributes for intersections, containment information, adjacency
     */
    public static class RectangleCollisionAttributes {

        @Nullable
        private final Vector2[] intersections;
        @Nullable
        private final ContainmentInfo containmentInfo;
        @Nullable
        private final Adjacency[] adjacencyList;

        public static final RectangleCollisionAttributes EMPTY = new RectangleCollisionAttributes(
                null, null, null
        );

        public RectangleCollisionAttributes(
                @Nullable Vector2[] intersections,
                @Nullable ContainmentInfo containmentInfo,
                @Nullable Adjacency[] adjacencyList
        ) {
            this.intersections = intersections;
            this.containmentInfo = containmentInfo;
            this.adjacencyList = adjacencyList;
        }
    }

    public record Rectangles(
            @Nullable Rectangle rectangle1,
            @Nullable Rectangle rectangle2
    ) {
        public static final Rectangles NONE = new Rectangles(null, null);
    }

    public record ContainmentInfo(
            boolean rect1ContainsRect2, boolean rect2ContainsRect1
    ) {}
}
