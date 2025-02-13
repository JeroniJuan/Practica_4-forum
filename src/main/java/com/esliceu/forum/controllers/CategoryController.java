package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.CategoryForm;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.repos.CategoriesRepo;
import com.esliceu.forum.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoriesService categoriesService;
    @CrossOrigin
    @GetMapping("/categories")
    public List<Category> getCategories(){
        return categoriesService.findAll();
    }

    @CrossOrigin
    @PostMapping("/categories")
    public void postCategory(@RequestBody CategoryForm categoryForm){
        Category category = new Category();
        category.setDescription(categoryForm.description());
        category.setTitle(categoryForm.title());
        categoriesService.save(category);
    }
}
