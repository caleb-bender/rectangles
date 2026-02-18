package com.caleb.rectangles.domain;

import java.util.HashMap;
import java.util.Map;

public record Rectangle(Vector2 topLeft, Size size) {
    /**
     * Creates a Rectangle object
     * @param topLeft The coordinates of the top left corner
     * @param size The size expanding from the top left (width, height)
     */
    public Rectangle {
        var invariantViolations = new HashMap<String, String[]>();
        if (size.width() <= 0)
            invariantViolations.put("size.width", new String[] { "The width must be greater than zero."});
        if (size.height() <= 0)
            invariantViolations.put("size.height", new String[] { "The height must be greater than zero."});
        if (!invariantViolations.isEmpty()) throw new InvalidRectangleException(invariantViolations);
    }
}
