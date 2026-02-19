package com.caleb.rectangles.domain;

import java.util.Map;

public class InvalidRectangleException extends RuntimeException {

    private final Map<String, String[]> invariantViolations;

    public InvalidRectangleException(Map<String, String[]> invariantViolations) {
        this.invariantViolations = invariantViolations;
    }

    public Map<String, String[]> invariantViolations() {
        return invariantViolations;
    }
}
