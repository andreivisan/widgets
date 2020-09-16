package com.miro.widgets.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widgets.exception.InvalidInputException;
import com.miro.widgets.model.Widget;
import com.miro.widgets.service.WidgetCrudService;
import com.miro.widgets.util.TestUtil;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "storage.h2=false")
@AutoConfigureMockMvc
public class WidgetControllerMockServiceTest {

    private final static String WIDGET_URL = "/widgets";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WidgetCrudService mockService;

    @Value("${storage.h2}") 
    private String useH2Storage;

    private MediaType contentType = new MediaType("application", "hal+json");


    @Test
    void whenCreateNullWidget_thenThrowException() throws Exception {
        Mockito.when(mockService.create(Mockito.any())).thenThrow(new InvalidInputException("Input is invalid"));

        mockMvc.perform(post(WIDGET_URL)).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    void whenCreateValidWidget_thenReturnTheWidget() throws Exception {
        Mockito.when(mockService.create(Mockito.any())).thenReturn(TestUtil.createWidgetWithZIndex());

        String response = mockMvc.perform(post(WIDGET_URL)
                .content(objectMapper.writeValueAsBytes(TestUtil.createWidgetWithZIndex()))
                .contentType(contentType))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(content().contentType(contentType))
            .andReturn().getResponse().getContentAsString();

        Widget widget = objectMapper.readValue(response, Widget.class);
        assertEquals(7, widget.getzIndex());
        assertNotNull(widget.getWidgetId());
    }

    @Test
    void whenH2StoragePropertyIsFalse_thenH2StorageIsFalse() {
        assertEquals("false", useH2Storage);
    }

}
