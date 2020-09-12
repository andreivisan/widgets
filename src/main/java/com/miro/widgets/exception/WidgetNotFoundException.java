package com.miro.widgets.exception;

public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException(Long id) {
        super("Could not find widget with id: " + id);
    }
    
}
