package com.miro.widgets.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.miro.widgets.exception.InvalidInputException;
import com.miro.widgets.exception.WidgetNotFoundException;
import com.miro.widgets.model.Widget;
import com.miro.widgets.model.WidgetModelAssembler;
import com.miro.widgets.repository.WidgetRepository;
import com.miro.widgets.service.WidgetCrudService;
import com.miro.widgets.service.WidgetDSServiceImpl;
import com.miro.widgets.service.WidgetH2ServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WidgetController {

    private final WidgetRepository repository;
    private final WidgetModelAssembler assembler;

    private WidgetCrudService crudService;
    
    @Value("${perPage.default}")
    private int perPageDefault;

    @Value("${perPage.max}")
    private int perPageMax;


    WidgetController(@Value("${storage.h2}") String useH2Storage, WidgetRepository repository, WidgetModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
        crudService = Boolean.valueOf(useH2Storage) ? new WidgetH2ServiceImpl(this.repository) : new WidgetDSServiceImpl(new TreeMap<>());
        
    }

    @PostMapping("/widgets")
    public ResponseEntity<?> newWidget(@RequestBody Widget newWidget) {
        Widget widget = crudService.create(newWidget).orElseThrow(() -> new InvalidInputException("Inputs are not valid"));
        EntityModel<Widget> widgetEntityModel = assembler.toModel(widget);

        return ResponseEntity
            .created(widgetEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(widgetEntityModel);
    }

    @GetMapping("/widgets/{id}")
    public EntityModel<Widget> singleWidget(@PathVariable Long id) {
        Widget widget = crudService.findWidgetById(id).orElseThrow(() -> new WidgetNotFoundException(id));
        
        return assembler.toModel(widget);
    }

    @GetMapping("/widgets")
    public CollectionModel<EntityModel<Widget>> allWidgets(@RequestParam(value = "page") Optional<Integer> pageOpt, @RequestParam(value = "perPage") Optional<Integer> perPageOpt) {
        var page = Math.max(1, pageOpt.orElse(1));
        var perPage = Math.min(perPageOpt.orElse(perPageDefault), perPageMax);
        
        List<EntityModel<Widget>> widgets = crudService.findAllWidgets(page, perPage).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(widgets,
            linkTo(methodOn(WidgetController.class).allWidgets(pageOpt, pageOpt)).withSelfRel());
    }

    @PutMapping("/widgets/{id}")
    public ResponseEntity<?> updateWidget(@RequestBody Widget newWidget, @PathVariable Long id) {
        Widget updatedWidget = crudService.update(newWidget, id).orElseThrow(() -> new InvalidInputException("Inputs are not valid"));

        EntityModel<Widget> widgetEntityModel = assembler.toModel(updatedWidget);

        return ResponseEntity
            .created(widgetEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(widgetEntityModel);
    }

    @DeleteMapping("/widgets/{id}")
    public ResponseEntity<?> deleteWidget(@PathVariable Long id) {
        crudService.deleteWidgetById(id);

        return ResponseEntity.noContent().build();
    }
    
}
