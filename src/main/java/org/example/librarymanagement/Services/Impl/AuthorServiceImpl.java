package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.AuthorEntity;
import org.example.librarymanagement.Model.DTO.AuthorDTO;
import org.example.librarymanagement.Model.Requests.AuthorRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.AuthorRepository;
import org.example.librarymanagement.Services.AuthorService;
import org.example.librarymanagement.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<AuthorDTO> getAllAuthors(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<AuthorEntity> authorEntities = authorRepository.findAll(pageable);
        List<AuthorDTO> authorDTOS = authorEntities.map(authorEntity -> {
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(authorEntity, authorDTO);
            return authorDTO;
        }).toList();
        return new PageImpl<>(authorDTOS, authorEntities.getPageable(), authorEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllAuthors() {
        DataResponse dataResponse = new DataResponse();
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        List<AuthorDTO> authorDTOS = authorEntities.stream().map(authorEntity -> {
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(authorEntity, authorDTO);
            return authorDTO;
        }).toList();
        dataResponse.setData(authorDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("OK");
        return dataResponse;
    }

    @Override
    public Object getAuthorById(String idAuthor) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try{
            AuthorEntity authorEntity = authorRepository.findById(idAuthor).get();
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(authorEntity, authorDTO);
            dataResponse.setData(authorDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addAuthor(AuthorRequest authorRequest) {
        MessageResponse messageResponse = new MessageResponse();
        AuthorEntity authorEntity = new AuthorEntity();
        modelMapper.map(authorRequest, authorEntity);
        authorEntity.setIdAuthor(RandomIdUtils.generateRandomId("AU", 15));
        authorEntity.setCreatedAt(LocalDateTime.now());
        authorEntity.setUpdatedAt(LocalDateTime.now());
        authorRepository.save(authorEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateAuthor(AuthorRequest authorRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            AuthorEntity authorEntity = authorRepository.findById(authorRequest.getIdAuthor()).get();
            modelMapper.map(authorRequest, authorEntity);
            authorEntity.setUpdatedAt(LocalDateTime.now());
            authorRepository.save(authorEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deleteAuthor(String idAuthor) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            AuthorEntity authorEntity = authorRepository.findById(idAuthor).get();
            authorRepository.delete(authorEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
