package com.miro.widgets.service;

import java.util.List;
import java.util.Optional;

import com.miro.widgets.model.Widget;
import com.miro.widgets.repository.WidgetRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

public class WidgetH2ServiceImpl implements WidgetCrudService {

    Logger logger = LoggerFactory.getLogger(WidgetH2ServiceImpl.class);

    private WidgetRepository repository;

    public WidgetH2ServiceImpl(WidgetRepository widgetRepository) {
        this.repository = widgetRepository;
    }

    @Override
    public void deleteWidgetById(Long id) {
        logger.info("Delete widget from H2 storage");
        repository.deleteById(id);
    }

    @Override
    public List<Widget> findAllWidgets() {
        logger.info("Get all widgets from H2 storage");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "zIndex"));
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        logger.info("Find widget by id from H2 storage");
        return repository.findById(id);
    }

    @Override
    public Widget save(Widget newWidget) {
        logger.info("Save widget using H2 storage");
        return repository.save(newWidget);
    }
    
}
