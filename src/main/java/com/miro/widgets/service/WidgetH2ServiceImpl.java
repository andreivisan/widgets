package com.miro.widgets.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.miro.widgets.exception.WidgetNotFoundException;
import com.miro.widgets.model.Widget;
import com.miro.widgets.repository.WidgetRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;

@Qualifier("H2Service")
public class WidgetH2ServiceImpl implements WidgetCrudService {

    private final static int Z_INDEX_SHIT = 1;

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
    public List<Widget> findAllWidgets(int page, int perPage) {
        logger.info("Get all widgets from H2 storage");
        List<Widget> widgetList = findAllWidgets();

        var startIndex = (page - 1) * perPage;
        if (startIndex >= widgetList.size()) {
            return Collections.EMPTY_LIST;
        }

        return widgetList.stream().skip(startIndex).limit(perPage).collect(Collectors.toList());
    }

    @Override
    public Optional<Widget> findWidgetById(Long id) {
        logger.info("Find widget by id from H2 storage");
        return repository.findById(id);
    }

    @Override
    public Optional<Widget> create(Widget newWidget) {
        logger.info("Create widget using H2 storage");

        if (validateInputs(newWidget)) {
            if (newWidget.getzIndex() == null) {
                Integer maxZIndex = maxZIndex();
                newWidget.setzIndex(maxZIndex + Z_INDEX_SHIT);
            }
    
            return Optional.of(insertWidget(newWidget));
        } else {
            return Optional.empty();
        } 
    }

    @Override
    public Optional<Widget> update(Widget newWidget, Long id) {
        logger.info("Update widget using H2 storage");

        Widget oldWidget = findWidgetById(id).orElseThrow(() -> new WidgetNotFoundException(id));
        Widget mergedWidget = merge(oldWidget, newWidget);

        if (validateInputs(mergedWidget)) {
            if (oldWidget.getzIndex() == mergedWidget.getzIndex()) {
                return Optional.of(repository.save(mergedWidget));
            }

            return Optional.of(insertWidget(mergedWidget));
        } else {
            return Optional.empty();
        }
        
    }

    private Widget insertWidget(Widget newWidget) {
        incrementZIndex(newWidget);

        return repository.save(newWidget);
    }

    private void incrementZIndex(Widget newWidget) {
        List<Widget> widgetList = findAllWidgets();
        Optional<Widget> fromWidget = findWidgetByZIndex(widgetList, newWidget);
        
        if (!fromWidget.isEmpty()) {
            logger.info("The new widget has same zIndex as an existing widget");
            
            List<Widget> tail = widgetList.subList(widgetList.indexOf(fromWidget.get()), widgetList.size());
            for (Widget widget : tail) {
                widget.setzIndex(widget.getzIndex() + 1);
            }
            repository.saveAll(tail);
        }
    }

    private Integer maxZIndex() {
        List<Widget> widgetList = findAllWidgets();
        if (widgetList.isEmpty()) {
            return 0;
        }
        return widgetList.get(widgetList.size() - 1).getzIndex();
    }

    private Optional<Widget> findWidgetByZIndex(List<Widget> widgetList, Widget widget) {
        int left = 0;
        int right = widgetList.size() - 1;

        while (left <= right) {
            int mid = left + ((right - left) / 2);

            if(widget.getzIndex() == widgetList.get(mid).getzIndex()) {
                return Optional.of(widgetList.get(mid));
            } else if(widget.getzIndex() < widgetList.get(mid).getzIndex()) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return Optional.empty();
    }

    private List<Widget> findAllWidgets() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "zIndex"));
    }
    
}
