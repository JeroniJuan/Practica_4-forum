package com.esliceu.forum.services;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.repos.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    @Autowired
    CategoriesRepo categoriesRepo;
    public List<Category> findAll() {
        return categoriesRepo.findAll();
    }

    public void save(Category category) {
        categoriesRepo.save(category);
    }

    public Category findByCategoryName(String category) {
        return categoriesRepo.findByTitle(category);
    }
}
