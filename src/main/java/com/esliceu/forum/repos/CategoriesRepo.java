package com.esliceu.forum.repos;

import com.esliceu.forum.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepo extends JpaRepository<Category, Integer> {

    Category findByTitle(String category);
}
