package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.Requests.CategoryRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    DataResponse getAllCategories();
    Object getCategory(Long idCategory);
    MessageResponse addCategory(CategoryRequest categoryRequest);
    MessageResponse updateCategory(CategoryRequest categoryRequest);
    MessageResponse deleteCategory(Long idCategory);
}
