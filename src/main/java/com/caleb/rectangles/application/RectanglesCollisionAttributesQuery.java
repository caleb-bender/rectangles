package com.caleb.rectangles.application;

public record RectanglesCollisionAttributesQuery(
        RectangleRawData rectangle1Data,
        RectangleRawData rectangle2Data,
        boolean queryIntersections,
        boolean queryContainment,
        boolean queryAdjacency
) {
    public record RectangleRawData(String x, String y, String w, String h) {

        String fieldAt(int i) {
            return switch (i) {
                case 0 -> x();
                case 1 -> y();
                case 2 -> w();
                case 3 -> h();
                default -> "";
            };
        }
    }
}
