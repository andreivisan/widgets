package com.miro.widgets.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Widget implements Comparable<Widget> {

    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer zIndex;
    private Integer widgetWidth;
    private Integer widgetHeight;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModificationDate;
    private @Id @GeneratedValue Long widgetId;

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getWidgetWidth() {
        return widgetWidth;
    }

    public void setWidgetWidth(Integer widgetWidth) {
        this.widgetWidth = widgetWidth;
    }

    public Integer getWidgetHeight() {
        return widgetHeight;
    }

    public void setWidgetHeight(Integer widgetHeight) {
        this.widgetHeight = widgetHeight;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    @Override
    public int compareTo(Widget otherWidget) {
        if (this.getzIndex() > otherWidget.getzIndex()) {
            return 1;
        } else if (this.getzIndex() < otherWidget.getzIndex()) {
            return -1;
        } else {
            return 0;
        }
    }
}
