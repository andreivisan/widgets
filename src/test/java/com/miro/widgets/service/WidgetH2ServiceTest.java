package com.miro.widgets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.miro.widgets.exception.WidgetNotFoundException;
import com.miro.widgets.model.Widget;
import com.miro.widgets.repository.WidgetRepository;
import com.miro.widgets.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WidgetH2ServiceTest {

    @Mock
    private WidgetRepository repository;

    private WidgetCrudService service;

    @BeforeEach
    void setup() {
        service = new WidgetH2ServiceImpl(repository);
    }

    @Test
    void whenSavingNullWidget_thenReturnOptionalEmpty() {
        assertEquals(Optional.empty(), service.create(null));
    }
    
}
