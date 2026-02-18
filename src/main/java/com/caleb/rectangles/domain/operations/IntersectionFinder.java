package com.caleb.rectangles.domain.operations;

import com.caleb.rectangles.domain.Rectangle;
import com.caleb.rectangles.domain.Vector2;

import java.util.ArrayList;

public class IntersectionFinder {

    public Vector2[] FindAll(Rectangle rectangleA, Rectangle rectangleB) {
        var intersections = new ArrayList<Vector2>();

        var rectALeftX = rectangleA.topLeft().x();
        var rectARightX = rectALeftX + rectangleA.size().width();
        var rectATopY = rectangleA.topLeft().y();
        var rectABottomY = rectATopY - rectangleA.size().height();

        var rectBTopLeft = rectangleB.topLeft();
        var rectBBottomRight = new Vector2(
                rectangleB.topLeft().x() + rectangleB.size().width(),
                   rectangleB.topLeft().y() - rectangleB.size().height()
        );

        if (rectBTopLeft.x() >= rectALeftX && rectBTopLeft.x() <= rectARightX)
            intersections.add(new Vector2(0,0));

        if (rectBTopLeft.y() < rectATopY && rectBTopLeft.y() > rectABottomY)
            intersections.add(new Vector2(0,0));

        if (rectBBottomRight.x() >= rectALeftX && rectBBottomRight.x() <= rectARightX)
            intersections.add(new Vector2(0,0));

        if (rectBBottomRight.y() < rectATopY && rectBBottomRight.y() > rectABottomY)
            intersections.add(new Vector2(0,0));



        return intersections.toArray(new Vector2[0]);
    }
}
