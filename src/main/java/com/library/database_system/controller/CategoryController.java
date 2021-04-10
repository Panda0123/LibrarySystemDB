package com.library.database_system.controller;

import com.library.database_system.projections.CategoryIdName;
import com.library.database_system.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController()
@RequestMapping(path = "api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // white list
    @GetMapping(path = "all")
    public Collection<CategoryIdName> getCategories() { return this.categoryService.getCategories(); }
}
