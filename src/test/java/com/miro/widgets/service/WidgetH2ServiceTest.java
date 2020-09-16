package com.miro.widgets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.miro.widgets.repository.WidgetRepository;
import com.miro.widgets.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

@DataJpaTest
public class WidgetH2ServiceTest {

    @Autowired
    private WidgetRepository repository;

    private WidgetCrudService service;

    @BeforeEach
    void setup() {
        service = new WidgetH2ServiceImpl(repository);

        service.create(TestUtil.createWidgetWithZIndex().get());
    }

    @Test
    void whenSavingNullWidget_thenReturnOptionalEmpty() {
        assertEquals(Optional.empty(), service.create(null));
    }

    @Test
    void whenSavingValidWidget_thenWidgetIsInH2DB() {
        assertEquals(1, service.findAllWidgets().size());
    }

    @Test
    void whenFindingWidgetWithInvalidId_thenReturnOptionalEmpty() {
        assertEquals(Optional.empty(), service.findWidgetById(100L));
    }

    @Test
    void whenFindingWidgetWithValidId_thenReturnWidget() {
        assertEquals(7, service.findWidgetById(1L).get().getzIndex());
    }

    @Test
    void whenDeletingWidgetWithInvalidId_thenExceptionIsThrows() throws Exception {
        assertThrows(EmptyResultDataAccessException.class, () -> service.deleteWidgetById(100L));
    }
    
}
