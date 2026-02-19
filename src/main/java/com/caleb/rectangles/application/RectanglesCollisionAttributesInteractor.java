package com.caleb.rectangles.application;

import com.caleb.rectangles.domain.operations.IAdjacencyFinder;
import com.caleb.rectangles.domain.operations.IIntersectionFinder;

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
        var rectangleDataParser = new RectangleRawDataParser();
        var rectangle1Fields = rectangleDataParser.parse(query.rectangle1Data(), "rectangle1", errors);
        var rectangle2Fields = rectangleDataParser.parse(query.rectangle2Data(), "rectangle2", errors);
        return new RectanglesCollisionAttributesResponse(false, errors);
    }

    private static void ensureAtLeastOneFlagIsSet(RectanglesCollisionAttributesQuery query, HashMap<String, String[]> errors) {
        if (!query.queryIntersections() && !query.queryContainment() &&
                !query.queryAdjacency())
            errors.put("queryFlags", new String[] { "At least one query flag must be set" });
    }
}
