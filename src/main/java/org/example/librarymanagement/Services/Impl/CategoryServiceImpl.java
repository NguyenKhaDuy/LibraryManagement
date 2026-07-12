package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.CategoryEntity;
import org.example.librarymanagement.Model.DTO.CategoryDTO;
import org.example.librarymanagement.Model.Requests.CategoryRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.CategoryRepository;
import org.example.librarymanagement.Services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public DataResponse getAllCategories() {
        DataResponse dataResponse = new DataResponse();
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categoryEntities.stream()
                .map(categoryEntity -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    modelMapper.map(categoryEntity, categoryDTO);
                    return categoryDTO;
                })
                .toList();
        dataResponse.setData(categoryDTOs);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getCategory(Long idCategory) {
        DataResponse dataResponse = new DataResponse();
        MessageResponse messageResponse = new MessageResponse();
        try{
            CategoryEntity categoryEntity = categoryRepository.findById(idCategory).get();
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(categoryEntity, categoryDTO);
            dataResponse.setData(categoryDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found category");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addCategory(CategoryRequest categoryRequest) {
        MessageResponse messageResponse = new MessageResponse();
        CategoryEntity categoryEntity = new CategoryEntity();
        modelMapper.map(categoryRequest, categoryEntity);
        categoryRepository.save(categoryEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateCategory(CategoryRequest categoryRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            CategoryEntity categoryEntity = categoryRepository.findById(categoryRequest.getIdCategory()).get();
            modelMapper.map(categoryRequest, categoryEntity);
            categoryRepository.save(categoryEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found category");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deleteCategory(Long idCategory) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            CategoryEntity categoryEntity = categoryRepository.findById(idCategory).get();
            categoryRepository.delete(categoryEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found category");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
