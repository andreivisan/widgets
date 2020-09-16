package com.miro.widgets.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.miro.widgets.model.Widget;

public interface WidgetCrudService {

    Optional<Widget> create(Widget newWidget);

    Optional<Widget> findWidgetById(Long id);

    List<Widget> findAllWidgets(int page, int perPage);

    Optional<Widget> update(Widget newWidget, Long id);

    void deleteWidgetById(Long id);

    default Widget merge(Widget oldWidget, Widget newWidget) {
        Widget merged = new Widget();
        
        merged.setWidgetId(oldWidget.getWidgetId());
        merged.setLastModificationDate(LocalDateTime.now());
        merged.setWidgetHeight(Optional.ofNullable(newWidget.getWidgetHeight()).orElse(oldWidget.getWidgetHeight()));
        merged.setWidgetWidth(Optional.ofNullable(newWidget.getWidgetWidth()).orElse(oldWidget.getWidgetWidth()));
        merged.setxCoordinate(Optional.ofNullable(newWidget.getxCoordinate()).orElse(oldWidget.getxCoordinate()));
        merged.setyCoordinate(Optional.ofNullable(newWidget.getyCoordinate()).orElse(oldWidget.getyCoordinate()));
        merged.setzIndex(Optional.ofNullable(newWidget.getzIndex()).orElse(oldWidget.getzIndex()));

        return merged;
    }

    default boolean validateInputs(Widget widget) {
        if (Objects.isNull(widget)) {
            return false;
        }

        if (Objects.isNull(widget.getxCoordinate()) || Objects.isNull(widget.getyCoordinate())) {
            return false;
        }

        if(Objects.isNull(widget.getWidgetHeight()) || Objects.isNull(widget.getWidgetWidth()) || widget.getWidgetHeight() < 0 || widget.getWidgetWidth() < 0) {
            return false;
        }

        return true;
    }
    
}
