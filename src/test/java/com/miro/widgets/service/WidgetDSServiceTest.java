package com.miro.widgets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.TreeMap;

import com.miro.widgets.exception.WidgetNotFoundException;
import com.miro.widgets.model.Widget;
import com.miro.widgets.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WidgetDSServiceTest {

    private WidgetCrudService service;

    @BeforeEach
    void setup() {
        TreeMap<Integer, Widget> repository = new TreeMap<>();
        Widget mockWidget = TestUtil.createWidgetWithZIndex().get();
        mockWidget.setWidgetId(0L);
        repository.put(TestUtil.createWidgetWithZIndex().get().getzIndex(), mockWidget);

        service = new WidgetDSServiceImpl(repository);
    }

    @Test
    void whenSavingNullWidget_thenReturnOptionalEmpty() {
        assertEquals(Optional.empty(), service.create(null));
    }

    @Test
    void whenSavingWidgetWithNoZIndex_thenMoveWidgetToForeground() {
        Widget widgetWithNoZIndex = service.create(TestUtil.createWidgetWithNoZIndex().get()).get();

        assertEquals(8, widgetWithNoZIndex.getzIndex());
        assertNotNull(widgetWithNoZIndex.getWidgetId());
        assertEquals(1, widgetWithNoZIndex.getWidgetId());
    }

    @Test
    void whenSavingWidgetWithExistingZIndex_thenMoveEqualOrGreaterInFront() {
        Widget widgetWithZIndex = service.create(TestUtil.createWidgetWithZIndex().get()).get();

        assertEquals(7, widgetWithZIndex.getzIndex());
        assertEquals(8, service.findWidgetById(0L).get().getzIndex());
    }

    @Test
    void whenFindWidgetByNonExistingId_thenReturnOptionalEmpty() {
        assertEquals(Optional.empty(), service.findWidgetById(100L));
    }

    @Test
    void whenFindWidgetByExistingId_thenReturnWidget() {
        assertEquals(7, service.findWidgetById(0L).get().getzIndex());
    }

    @Test
    void whenFindAllWidgets_returnInAscendingOrderByZIndex() {
        Widget mockWidget = TestUtil.createWidgetWithNoZIndex().get();

        //Add 15 widgets for testing pagination
        for(int i = 0; i < 15; i++) {
            service.create(mockWidget);
        }
            
        assertEquals(7, service.findAllWidgets(1, 7).size());
        assertTrue(service.findAllWidgets(1, 7).get(0).getzIndex() < service.findAllWidgets(1, 7).get(1).getzIndex());
    }

    @Test
    void whenUpdateWidgetWithValidInput_thenReturnUpdatedwidget() {
        Widget newWidget = new Widget();
        newWidget.setWidgetWidth(250);

        service.update(newWidget, 0L);

        assertEquals(250, service.findWidgetById(0L).get().getWidgetWidth());
    }

    @Test
    void whenUpdateANonExistingWidget_thenThrowException() {
        Widget newWidget = new Widget();
        newWidget.setWidgetWidth(250);

        assertThrows(WidgetNotFoundException.class, () -> service.update(newWidget, 25L));
    }

    @Test
    void whenUpdateWidgetWithNegativeWidth_thenReturnOptionalEmpty() {
        Widget newWidget = TestUtil.createWidgetWithNegativeWidth().get();

        assertEquals(Optional.empty(), service.update(newWidget, 0L));
    }

    @Test
    void whenDeleteWidgetWithValidId_thenWidgetIsRemoved() {
        service.deleteWidgetById(0L);

        assertTrue(service.findAllWidgets(1, 1).isEmpty());
    }
     
}
