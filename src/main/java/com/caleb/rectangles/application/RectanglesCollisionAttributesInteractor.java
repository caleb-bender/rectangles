package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;
import com.caleb.rectangles.domain.operations.Adjacency;
import com.caleb.rectangles.domain.operations.IAdjacencyFinder;
import com.caleb.rectangles.domain.operations.IIntersectionFinder;

import static com.caleb.rectangles.application.RectanglesCollisionAttributesResponse.*;

import java.util.HashMap;

public class RectanglesCollisionAttributesInteractor {

    private final IAdjacencyFinder adjacencyFinder;
    private final IIntersectionFinder intersectionFinder;

    public RectanglesCollisionAttributesInteractor(
            IAdjacencyFinder adjacencyFinder,
            IIntersectionFinder intersectionFinder
    ) {
        this.adjacencyFinder = adjacencyFinder;
        this.intersectionFinder = intersectionFinder;
    }

    /**
     * Given a query containing two rectangles and one or more flags, this method will return a response
     * containing all relevant attributes
     * @param query
     * @return relevant collision attributes of the two rectangles
     */
    public RectanglesCollisionAttributesResponse Execute(
            RectanglesCollisionAttributesQuery query
    ) {
        var errors = new HashMap<String, String[]>();
        ensureAtLeastOneFlagIsSet(query, errors);
        var rectangleParser = new RectangleRawDataParser();
        var rect1Optional = rectangleParser.parse(query.rectangle1Data(), "rectangle1", errors);
        var rect2Optional = rectangleParser.parse(query.rectangle2Data(), "rectangle2", errors);
        var thereAreErrorsOrOneOfTheRectanglesAreMissing =
                !errors.isEmpty() || rect1Optional.isEmpty() || rect2Optional.isEmpty();
        if (thereAreErrorsOrOneOfTheRectanglesAreMissing) return errorResponse(errors);
        var rect1 = rect1Optional.get();
        var rect2 = rect2Optional.get();
        var attributes = getCollisionAttributes(query, rect1, rect2);
        return new RectanglesCollisionAttributesResponse(
            errors, attributes, new Rectangles(rect1, rect2)
        );
    }

    private RectangleCollisionAttributes getCollisionAttributes(
            RectanglesCollisionAttributesQuery query, Rectangle rect1, Rectangle rect2
    ) {
        Vector2[] intersections = null;
        ContainmentInfo containmentInfo = null;
        Adjacency[] adjacencyList = null;
        if (query.queryIntersections())
            intersections = intersectionFinder.findAll(rect1, rect2);
        if (query.queryContainment())
            containmentInfo = new ContainmentInfo(
                    rect1.contains(rect2),
                    rect2.contains(rect1)
            );
        if (query.queryAdjacency())
            adjacencyList = adjacencyFinder.findAll(rect1, rect2);
        return new RectangleCollisionAttributes(intersections, containmentInfo, adjacencyList);
    }

    private static RectanglesCollisionAttributesResponse errorResponse(HashMap<String, String[]> errors) {
        return new RectanglesCollisionAttributesResponse(
                errors, RectangleCollisionAttributes.EMPTY, Rectangles.NONE
        );
    }

    private static void ensureAtLeastOneFlagIsSet(RectanglesCollisionAttributesQuery query, HashMap<String, String[]> errors) {
        if (!query.queryIntersections() && !query.queryContainment() &&
                !query.queryAdjacency())
            errors.put("queryFlags", new String[] { "At least one query flag must be set" });
    }
}
