package com.miro.widgets.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
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
        
        Optional<Widget> widgetToUpdate = findWidgetById(newWidget.getWidgetId());
        
        if (widgetToUpdate.isEmpty()) {
            newWidget.setWidgetId(counter.incrementAndGet());
            if (newWidget.getzIndex() == null) {    
                Integer maxZIntex = maxZIndex(repository);
                newWidget.setzIndex(maxZIntex + 1);
            }
        }

        if (widgetToUpdate.isPresent() && widgetToUpdate.get().getzIndex() == newWidget.getzIndex()) {
            repository.put(newWidget.getzIndex(), newWidget);
            return newWidget;
        }

        repository = insertWidget(repository, newWidget);
        
        return newWidget;
    }

    private TreeMap<Integer, Widget> insertWidget(TreeMap<Integer, Widget> source, Widget newWidget) {
        if (source.containsKey(newWidget.getzIndex())) {
            TreeMap<Integer, Widget> result = new TreeMap<>();

            result.putAll(source.headMap(newWidget.getzIndex()));
            result.put(newWidget.getzIndex(), newWidget);

            SortedMap<Integer, Widget> tail = source.tailMap(newWidget.getzIndex());
            for (Integer key : tail.keySet()) {
                Widget widget = incrementZIndex(tail.get(key));
                result.put(widget.getzIndex(), widget);
            }

            return result;
        } else {
            source.put(newWidget.getzIndex(), newWidget);
            return source;
        }
    }

    private Widget incrementZIndex(Widget widget) {
        widget.setzIndex(widget.getzIndex() + 1);
        return widget;
    }

    private Integer maxZIndex(TreeMap<Integer, Widget> input) {
        return Collections.max(input.keySet());
    }
    
}
