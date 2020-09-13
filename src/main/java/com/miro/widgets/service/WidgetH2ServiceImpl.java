package com.miro.widgets.service;

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
        return insertWidget(newWidget);
    }

    private Widget insertWidget(Widget newWidget) {
        List<Widget> widgetList = findAllWidgets();

        Widget widgetToUpdate = findWidgetById(widgetList, newWidget);

        if (widgetToUpdate == null) {
            if (newWidget.getzIndex() == null) {
                Integer maxZIndex = maxZIndex(widgetList);
                newWidget.setzIndex(maxZIndex + 1);
            }
        }

        if (widgetToUpdate != null && widgetToUpdate.getzIndex() == newWidget.getzIndex()) {
            return repository.save(newWidget);
        }
        
        incrementZIndex(widgetList, newWidget);

        return repository.save(newWidget);
    }

    private void incrementZIndex(List<Widget> widgetList, Widget newWidget) {
        Widget fromWidget = findWidgetByZIndex(widgetList, newWidget);
        
        if (fromWidget != null) {
            logger.info("The new widget has same zIndex as an existing widget");
            
            List<Widget> tail = widgetList.subList(widgetList.indexOf(fromWidget), widgetList.size());
            for (Widget widget : tail) {
                widget.setzIndex(widget.getzIndex() + 1);
            }
            repository.saveAll(tail);
        }
    }

    private Integer maxZIndex(List<Widget> input) {
        return Collections.max(input).getzIndex();
    }

    private Widget findWidgetByZIndex(List<Widget> widgetList, Widget widget) {
        int left = 0;
        int right = widgetList.size() - 1;

        while(left <= right) {
            int mid = left + ((right - left) / 2);

            if(widget.getzIndex() == widgetList.get(mid).getzIndex()) {
                return widgetList.get(mid);
            } else if(widget.getzIndex() < widgetList.get(mid).getzIndex()) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return null;
    }

    private Widget findWidgetById(List<Widget> widgetList, Widget widget) {
        for (Widget widgetItem : widgetList) {
            if (widgetItem.getWidgetId() == widget.getWidgetId()) {
                return widgetItem;
            }
        }
        
        return null;
    }
    
}
