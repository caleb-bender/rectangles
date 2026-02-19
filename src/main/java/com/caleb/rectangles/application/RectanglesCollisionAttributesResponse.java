package com.caleb.rectangles.application;

import java.util.Map;

public record RectanglesCollisionAttributesResponse(
        boolean isSuccess,
        Map<String, String[]> errors
) {
}
