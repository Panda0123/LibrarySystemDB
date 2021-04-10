package com.library.database_system.service;

import com.library.database_system.projections.CategoryIdName;
import com.library.database_system.domain.Category;
import com.library.database_system.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

//    public List<CategoryIdName> getCategories() {
//        return this.categoryRepository.findAll();
//    }

    public Collection<CategoryIdName> getCategories() {
        return this.categoryRepository.getIdName();
    }

    public Category getCategory(Long id) {
        Category cat = this.categoryRepository.findById(id).orElseThrow(
                () -> { return new IllegalStateException("CategoryID:" + id + " does not exist");});
        return cat;
    }

    public void addNewCategory(Category cat) {
        Optional<Category> catOptional = this.categoryRepository.findByName(cat.getName());

        if(catOptional.isPresent()) {
            throw new IllegalStateException("Category already exist");
        }
        this.categoryRepository.save(cat);
    }

    public void deleteCategory(Long catId){
        boolean doesExist = this.categoryRepository.existsById(catId);

        if (doesExist) {
            this.categoryRepository.deleteById(catId);
        } else {
            throw new IllegalStateException("Book does not exist");
        }
    }

    @Transactional
    public void updateCategory(Long categoryId, String name) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(
                () -> { throw new IllegalStateException("CategoryID:" + categoryId + " does not exist");});

        if (name != null && !Objects.equals(category.getName(), name)) {
            category.setName(name);
        }
    }

    public boolean isEmpty() {
        return this.categoryRepository.findAll().size() == 0;
    }

    public void saveAll(List<Category> ltCats) {
        this.categoryRepository.saveAll(ltCats);
    }
}