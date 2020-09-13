package com.miro.widgets.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.miro.widgets.model.Widget;
import com.miro.widgets.repository.WidgetRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

public class WidgetH2ServiceImpl implements WidgetCrudService {

    Logger logger = LoggerFactory.getLogger(WidgetH2ServiceImpl.class);

    private List<Widget> allWidgets;

    private WidgetRepository repository;

    public WidgetH2ServiceImpl(WidgetRepository widgetRepository) {
        this.repository = widgetRepository;
        this.allWidgets = new ArrayList<>();
    }

    @Override
    public void deleteWidgetById(Long id) {
        logger.info("Delete widget from H2 storage");
        repository.deleteById(id);
    }

    @Override
    public List<Widget> findAllWidgets() {
        logger.info("Get all widgets from H2 storage");
        
        allWidgets = repository.findAll(Sort.by(Sort.Direction.ASC, "zIndex"));

        return allWidgets;
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        logger.info("Find widget by id from H2 storage");
        return repository.findById(id);
    }

    @Override
    public Widget save(Widget newWidget) {
        logger.info("Save widget using H2 storage");

        if (newWidget.getzIndex() == null) {
            Integer maxZIndex = maxZIndex(allWidgets);
            newWidget.setzIndex(maxZIndex + 1);
        }

        return repository.save(newWidget);
    }

    private Integer maxZIndex(List<Widget> input) {
        return Collections.max(input).getzIndex();
    }
    
}
