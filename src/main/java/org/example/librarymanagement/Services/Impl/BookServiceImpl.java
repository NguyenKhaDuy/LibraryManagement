package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.*;
import org.example.librarymanagement.Model.DTO.*;
import org.example.librarymanagement.Model.Requests.BookRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.*;
import org.example.librarymanagement.Services.BooksService;
import org.example.librarymanagement.Utils.ConvertByteToBase64;
import org.example.librarymanagement.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BooksService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookshelfRepository bookshelfRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PublishingHouseRepository publishingHouseRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<BooksDTO> getBooks(String idAuthor, Long idCategory, Long idBookShelf, Long idPublisher, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        AuthorEntity authorEntity = null;
        CategoryEntity categoryEntity = null;
        BookshelfEntity bookshelfEntity = null;
        PublishingHouseEntity publishingHouseEntity = null;
        try {
            if (idAuthor != null) { authorEntity = authorRepository.findById(idAuthor).get();}
            if (idCategory != null) { categoryEntity = categoryRepository.findById(idCategory).get();}
            if (idBookShelf != null) {bookshelfEntity = bookshelfRepository.findById(idBookShelf).get();}
            if (idPublisher != null) {publishingHouseEntity = publishingHouseRepository.findById(idPublisher).get();}
        }catch (NoSuchElementException e){
            e.printStackTrace();
            return null;
        }
        Page<BooksEntity> booksEntities = bookRepository.searchBooks(authorEntity, bookshelfEntity, categoryEntity, publishingHouseEntity, pageable);
        List<BooksDTO> booksDTOS = booksEntities.stream().map(booksEntity -> {
            BooksDTO booksDTO = new BooksDTO();

            modelMapper.map(booksEntity, booksDTO);

            //IMAGE
            List<ImageDTO> imageDTOS = booksEntity.getImageEntities().stream().map(imageEntity -> {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setIdImage(imageEntity.getIdImage());
                imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                return imageDTO;
            }).toList();
            booksDTO.setImageDTOS(imageDTOS);

            //tác giả
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(booksEntity.getAuthorEntity(), authorDTO);
            booksDTO.setAuthorDTO(authorDTO);

            //catefory
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(booksEntity.getCategoryEntity(), categoryDTO);
            booksDTO.setCategoryDTO(categoryDTO);

            //kệ sách
            BookshelfDTO bookshelfDTO = new BookshelfDTO();
            modelMapper.map(booksEntity.getBookshelfEntity(), bookshelfDTO);
            booksDTO.setBookshelfDTO(bookshelfDTO);

            //nhà xuất bản
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(booksEntity.getPublishingHouseEntity(), publishingHouseDTO);
            booksDTO.setPublishingHouseDTO(publishingHouseDTO);

            return booksDTO;
        }).toList();
        return new PageImpl<>(booksDTOS, booksEntities.getPageable(), booksEntities.getTotalElements());
    }

    @Override
    public Object getBooksById(String idBook) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            BooksEntity booksEntity = bookRepository.findById(idBook).get();
            BooksDTO booksDTO = new BooksDTO();
            modelMapper.map(booksEntity, booksDTO);

            //IMAGE
            List<ImageDTO> imageDTOS = booksEntity.getImageEntities().stream().map(imageEntity -> {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setIdImage(imageEntity.getIdImage());
                imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                return imageDTO;
            }).toList();
            booksDTO.setImageDTOS(imageDTOS);

            //tác giả
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(booksEntity.getAuthorEntity(), authorDTO);
            booksDTO.setAuthorDTO(authorDTO);

            //catefory
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(booksEntity.getCategoryEntity(), categoryDTO);
            booksDTO.setCategoryDTO(categoryDTO);

            //kệ sách
            BookshelfDTO bookshelfDTO = new BookshelfDTO();
            modelMapper.map(booksEntity.getBookshelfEntity(), bookshelfDTO);
            booksDTO.setBookshelfDTO(bookshelfDTO);

            //nhà xuất bản
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(booksEntity.getPublishingHouseEntity(), publishingHouseDTO);
            booksDTO.setPublishingHouseDTO(publishingHouseDTO);

            dataResponse.setData(booksDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addBook(BookRequest bookRequest) {
        MessageResponse messageResponse = new MessageResponse();
        AuthorEntity authorEntity = null;
        BookshelfEntity bookshelfEntity = null;
        PublishingHouseEntity publishingHouseEntity = null;
        CategoryEntity categoryEntity = null;
        try {
            authorEntity = authorRepository.findById(bookRequest.getAuthorId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            bookshelfEntity = bookshelfRepository.findById(bookRequest.getBookShelfId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such book shelf");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            publishingHouseEntity = publishingHouseRepository.findById(bookRequest.getPublisherId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such publishing house");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            categoryEntity = categoryRepository.findById(bookRequest.getCategoryId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such category");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        BooksEntity booksEntity = new BooksEntity();
        modelMapper.map(bookRequest, booksEntity);
        booksEntity.setIdBook(RandomIdUtils.generateRandomId("B", 15));

        bookRequest.getImages().stream().forEach(image -> {
            try {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImage(image.getBytes());
                booksEntity.getImageEntities().add(imageEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        booksEntity.setStatus(Status.AVAILABLE);
        booksEntity.setCreatedAt(LocalDateTime.now());
        booksEntity.setUpdatedAt(LocalDateTime.now());
        booksEntity.setAuthorEntity(authorEntity);

        if (bookshelfEntity.getCurrentCapacity() == 0){
            messageResponse.setMessage("Book shelf is not empty");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }

        booksEntity.setBookshelfEntity(bookshelfEntity);
        booksEntity.setPublishingHouseEntity(publishingHouseEntity);
        booksEntity.setCategoryEntity(categoryEntity);
        bookRepository.save(booksEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateBook(BookRequest bookRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BooksEntity booksEntity = null;
        AuthorEntity authorEntity = null;
        BookshelfEntity bookshelfEntity = null;
        PublishingHouseEntity publishingHouseEntity = null;
        CategoryEntity categoryEntity = null;
        try {
            booksEntity = bookRepository.findById(bookRequest.getIdBook()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such books");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            authorEntity = authorRepository.findById(bookRequest.getAuthorId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such author");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            bookshelfEntity = bookshelfRepository.findById(bookRequest.getBookShelfId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such book shelf");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            publishingHouseEntity = publishingHouseRepository.findById(bookRequest.getPublisherId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such publishing house");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            categoryEntity = categoryRepository.findById(bookRequest.getCategoryId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such category");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(bookRequest, booksEntity);

        booksEntity.getImageEntities().clear();
        List<ImageEntity> imageEntities = new ArrayList<>();
        bookRequest.getImages().stream().forEach(image -> {
            try {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImage(image.getBytes());
               imageEntities.add(imageEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        booksEntity.setImageEntities(imageEntities);
        booksEntity.setStatus(bookRequest.getStatus());
        booksEntity.setAuthorEntity(authorEntity);
        if (bookshelfEntity.getCurrentCapacity() == 0){
            messageResponse.setMessage("Book shelf is not empty");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
        booksEntity.setPublishingHouseEntity(publishingHouseEntity);
        booksEntity.setCategoryEntity(categoryEntity);
        booksEntity.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(booksEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteBook(String idBook) {
        MessageResponse messageResponse = new MessageResponse();
        BooksEntity booksEntity = null;
        try {
            booksEntity = bookRepository.findById(idBook).get();
            bookRepository.delete(booksEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such books");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
