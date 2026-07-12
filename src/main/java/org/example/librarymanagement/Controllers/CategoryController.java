package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.Requests.CategoryRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/api/category")
    public ResponseEntity<Object> getAllCategories() {
        DataResponse dataResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/id-category={id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long id) {
        Object result = categoryService.getCategory(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/category")
    public ResponseEntity<Object> addCategory(@RequestBody CategoryRequest categoryRequest) {
        MessageResponse messageResponse = categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/admin/category")
    public ResponseEntity<Object> updateCategory(@RequestBody CategoryRequest categoryRequest) {
        MessageResponse messageResponse = categoryService.updateCategory(categoryRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/admin/category/id-category={id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        MessageResponse messageResponse = categoryService.deleteCategory(id);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
