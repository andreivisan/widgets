package com.miro.widgets.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import com.miro.widgets.model.Widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetDSServiceImpl implements WidgetCrudService {

    Logger logger = LoggerFactory.getLogger(WidgetDSServiceImpl.class);

    private TreeMap<Integer, Widget> repository;
    private AtomicLong counter = new AtomicLong(0);

    public WidgetDSServiceImpl(TreeMap<Integer, Widget> mapRepository) {
        this.repository = mapRepository;
    }

    @Override
    public void deleteWidgetById(Long id) {
        logger.info("Delete widget from map storage");
        if (!repository.entrySet().isEmpty()) {
            for (Widget widget : repository.values()) {
                if (widget.getWidgetId() == id) {
                    repository.remove(widget.getzIndex());
                }
            }
        }
    }

    @Override
    public List<Widget> findAllWidgets() {
        logger.info("Find all widgets from map storage");
        List<Widget> widgets = new ArrayList<>();
        widgets.addAll(repository.values());
        return widgets;
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        logger.info("Find widget by id from map storage");
        for (Widget widget : repository.values()) {
            if (widget.getWidgetId() == id) {
                return Optional.of(widget);
            }
        }

        return Optional.empty();
    }

    @Override
    public Widget save(Widget newWidget) {
        logger.info("Save widget using map storage");
        
        newWidget.setWidgetId(counter.incrementAndGet());
        if (newWidget.getzIndex() == null) {    
            Integer maxZIntex = maxZIndex(repository);
            newWidget.setzIndex(maxZIntex + 1);
        }
        repository.put(newWidget.getzIndex(), newWidget);
        
        return newWidget;
    }

    private Integer maxZIndex(TreeMap<Integer, Widget> input) {
        return Collections.max(input.keySet());
    }
    
}
