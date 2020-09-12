package com.miro.widgets.service;

import java.util.List;
import java.util.Optional;

import com.miro.widgets.model.Widget;

public interface WidgetCrudService {

    Widget save(Widget newWidget);

    Optional<Widget> findWidgetById(Long id);

    List<Widget> findAllWidgets();

    void deleteWidgetById(Long id);
    
}
