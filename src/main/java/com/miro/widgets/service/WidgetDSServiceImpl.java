package com.miro.widgets.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.miro.widgets.exception.WidgetNotFoundException;
import com.miro.widgets.model.Widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetDSServiceImpl implements WidgetCrudService {

    private final static int Z_INDEX_SHIT = 1;

    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    Logger logger = LoggerFactory.getLogger(WidgetDSServiceImpl.class);

    private TreeMap<Integer, Widget> repository;
    private AtomicLong counter = new AtomicLong(0);

    public WidgetDSServiceImpl(TreeMap<Integer, Widget> mapRepository) {
        var lock = new ReentrantReadWriteLock();
        this.repository = mapRepository;
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public void deleteWidgetById(Long id) {
        logger.info("Delete widget from map storage");
        List<Widget> widgetList = new ArrayList<>(repository.values());

        writeLock.lock();
        try {
            if (!repository.entrySet().isEmpty()) {
                for (Widget widget : widgetList) {
                    if (widget.getWidgetId() == id) {
                        repository.remove(widget.getzIndex());
                    }
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Widget> findAllWidgets() {
        logger.info("Find all widgets from map storage");
        List<Widget> widgets = new ArrayList<>();

        readLock.lock();
        try {
            widgets.addAll(repository.values());
        } finally {
            readLock.unlock();
        }

        return widgets;
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        logger.info("Find widget by id from map storage");

        readLock.lock();
        try {   
            for (Widget widget : repository.values()) {
                if (widget.getWidgetId() == id) {
                    return Optional.of(widget);
                }
            }
        } finally {
            readLock.unlock();
        }

        return Optional.empty();
    }

    @Override
    public Widget create(Widget newWidget) {
        logger.info("Create widget using map storage");

        newWidget.setWidgetId(counter.incrementAndGet());
        
        if (newWidget.getzIndex() == null) {    
            Integer maxZIntex = maxZIndex(repository);
            newWidget.setzIndex(maxZIntex + Z_INDEX_SHIT);
        }

        writeLock.lock();
        try {
            insertWidget(newWidget);
        } finally {
            writeLock.unlock();
        }

        return newWidget;
    }

    @Override
    public Widget update(Widget newWidget, Long id) {
        logger.info("Update widget using map storage");

        Widget oldWidget = findWidgetById(id).orElseThrow(() -> new WidgetNotFoundException(id));
        Widget mergedWidget = merge(oldWidget, newWidget);

        writeLock.lock();
        try {
            if (oldWidget.getzIndex() == mergedWidget.getzIndex()) {
                repository.replace(oldWidget.getzIndex(), mergedWidget);
            } else {
                repository.remove(oldWidget.getzIndex());
                insertWidget(mergedWidget);
            }
        } finally {
            writeLock.unlock();
        }

        return mergedWidget;
    }

    private void insertWidget(Widget newWidget) {
        if (repository.containsKey(newWidget.getzIndex())) {
            TreeMap<Integer, Widget> result = new TreeMap<>();

            result.putAll(repository.headMap(newWidget.getzIndex()));
            result.put(newWidget.getzIndex(), newWidget);

            SortedMap<Integer, Widget> tail = repository.tailMap(newWidget.getzIndex());
            for (Integer key : tail.keySet()) {
                Widget widget = incrementZIndex(tail.get(key));
                result.put(widget.getzIndex(), widget);
            }

            repository.clear();
            repository.putAll(result);
        } else {
            repository.put(newWidget.getzIndex(), newWidget);
        }
    }

    private Widget incrementZIndex(Widget widget) {
        widget.setzIndex(widget.getzIndex() + Z_INDEX_SHIT);
        return widget;
    }

    private Integer maxZIndex(TreeMap<Integer, Widget> input) {
        if(input.isEmpty()) {
            return 0;
        }
        return input.lastKey();
    }
    
}
