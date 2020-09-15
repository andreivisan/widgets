package com.miro.widgets.util;

import java.util.Optional;

import com.miro.widgets.model.Widget;

public class TestUtil {


    public static Optional<Widget> createWidgetWithNoZIndex() {
        Widget widget = new Widget();

        widget.setWidgetHeight(70);
        widget.setWidgetWidth(70);
        widget.setxCoordinate(100);
        widget.setyCoordinate(80);

        return Optional.of(widget);
    }

    public static Optional<Widget> createWidgetWithZIndex() {
        Widget widget = new Widget();

        widget.setWidgetHeight(50);
        widget.setWidgetWidth(50);
        widget.setxCoordinate(70);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);
    }

    public static Optional<Widget> createWidgetWithNegativeWidth() {
        Widget widget = new Widget();

        widget.setWidgetHeight(50);
        widget.setWidgetWidth(-50);
        widget.setxCoordinate(70);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);

    }

    public static Optional<Widget> createWidgetWithNegativeHeight() {
        Widget widget = new Widget();

        widget.setWidgetHeight(-50);
        widget.setWidgetWidth(50);
        widget.setxCoordinate(70);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);
    }
    
    public static Optional<Widget> createWidgetWithNullWidth() {
        Widget widget = new Widget();

        widget.setWidgetHeight(50);
        widget.setWidgetWidth(null);
        widget.setxCoordinate(70);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);
    }

    public static Optional<Widget> createWidgetWithNullHeight() {
        Widget widget = new Widget();

        widget.setWidgetHeight(null);
        widget.setWidgetWidth(50);
        widget.setxCoordinate(70);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);
    }

    public static Optional<Widget> createWidgetWithNullXCoordinate() {
        Widget widget = new Widget();

        widget.setWidgetHeight(50);
        widget.setWidgetWidth(50);
        widget.setxCoordinate(null);
        widget.setyCoordinate(70);
        widget.setzIndex(7);

        return Optional.of(widget);
    }

    public static Optional<Widget> createWidgetWithNullYCoordinate() {
        Widget widget = new Widget();

        widget.setWidgetHeight(50);
        widget.setWidgetWidth(50);
        widget.setxCoordinate(70);
        widget.setyCoordinate(null);
        widget.setzIndex(7);

        return Optional.of(widget);
    }
}
