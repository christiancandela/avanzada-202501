package com.example.demo.services;

import com.example.demo.dtos.CategoryRequest;
import com.example.demo.dtos.CategoryResponse;

import java.util.List;

public interface CategoryServices {
    CategoryResponse save(CategoryRequest category);
    CategoryResponse update(String id,CategoryRequest category);
    List<CategoryResponse> findAll();
    CategoryResponse findById(String id);
    void deleteById(String id);
}
