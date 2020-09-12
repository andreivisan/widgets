package com.miro.widgets.repository;

import com.miro.widgets.model.Widget;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WidgetRepository extends JpaRepository<Widget, Long> {
    
}
