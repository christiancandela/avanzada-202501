package com.example.demo.services;

import com.example.demo.domain.Category;
import com.example.demo.domain.CategoryStatus;
import com.example.demo.dtos.CategoryRequest;
import com.example.demo.dtos.CategoryResponse;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.mappers.CategoryMapper;
import com.example.demo.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServicesImpl implements CategoryServices{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse save(CategoryRequest category) {
        var newCategory = categoryMapper.parseOf(category);
        validateCategoryName(category.name());
        return categoryMapper.toCategoryResponse(
                categoryRepository.save(newCategory)
        );
    }

    @Override
    public CategoryResponse update(String id,CategoryRequest category) {
        var updatedCategory = findCategoryById(id);
        updatedCategory.setName(category.name());
        if( !updatedCategory.getName().equals(category.name()) ){
            validateCategoryName(category.name());
        }
        updatedCategory.setDescription(category.description());
        return categoryMapper.toCategoryResponse(
                categoryRepository.save(updatedCategory)
        );
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(String id) {
        return categoryMapper.toCategoryResponse(findCategoryById(id));
    }

    @Override
    public void deleteById(String id) {
        var categoryStored = findCategoryById(id);
        categoryStored.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(categoryStored);
    }

    private Category findCategoryById(String id){
        var storedCategory = categoryRepository.findById(id);
//        if(storedCategory.isEmpty()) {
//            throw new ResourceNotFoundException();
//        }
        return storedCategory.orElseThrow(ResourceNotFoundException::new);
    }

    private void validateCategoryName(String categoryName) {
        var category = categoryRepository.findByName(categoryName);
        if(category.isPresent()) {
            throw new ValueConflictException("Category name already exists");
        }
    }
}
