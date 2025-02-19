package com.esliceu.forum.services;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.repos.CategoriesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean deleteByTitle(String categoryName) {
        if (categoriesRepo.existsByTitle(categoryName)){
            Category category = categoriesRepo.findByTitle(categoryName);
            categoriesRepo.delete(category);
            return true;
        }
        return false;
    }

    public Map<String, String[]> getCategoryPermissions(int userId) {
        Map<String, String[]> categoryPermissions = new HashMap<>();

        List<Category> categories = findAll();

        String[] permissions = {
                "categories_topics:write",
                "categories_topics:delete",
                "categories_replies:write",
                "categories_replies:delete"
        };

        for (Category category : categories) {
            int[] moderators = category.getModerators();
            if (moderators != null){
                if (Arrays.stream(moderators).anyMatch(moderatorId -> moderatorId == userId)) {
                    categoryPermissions.put(category.getTitle(), permissions);
                }
            }
        }

        return categoryPermissions;
    }

}
