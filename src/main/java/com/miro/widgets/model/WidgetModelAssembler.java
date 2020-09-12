package com.miro.widgets.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.miro.widgets.controller.WidgetController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class WidgetModelAssembler implements RepresentationModelAssembler<Widget, EntityModel<Widget>> {

    @Override
    public EntityModel<Widget> toModel(Widget widget) {
        return EntityModel.of(widget, 
            linkTo(methodOn(WidgetController.class).singleWidget(widget.getWidgetId())).withSelfRel(),
            linkTo(methodOn(WidgetController.class).allWidgets()).withRel("widgets"));
    }
    
}
