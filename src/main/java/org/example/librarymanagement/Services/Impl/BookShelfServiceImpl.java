package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.BookshelfEntity;
import org.example.librarymanagement.Entity.Status;
import org.example.librarymanagement.Model.DTO.BookshelfDTO;
import org.example.librarymanagement.Model.Requests.BookShelfRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.BookshelfRepository;
import org.example.librarymanagement.Services.BookShelfService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookShelfServiceImpl implements BookShelfService {
    @Autowired
    BookshelfRepository bookshelfRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<BookshelfDTO> getAllBookShelf(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<BookshelfEntity> bookshelfEntities = bookshelfRepository.findAll(pageable);
        List<BookshelfDTO> bookshelfDTOS = bookshelfEntities.stream().map(bookshelfEntity -> {
            BookshelfDTO bookshelfDTO = new BookshelfDTO();
            modelMapper.map(bookshelfEntity, bookshelfDTO);
            return bookshelfDTO;
        }).toList();
        return new PageImpl<>(bookshelfDTOS, bookshelfEntities.getPageable(), bookshelfEntities.getTotalElements());
    }

    @Override
    public Object getBookShelfById(Long idBookshelf) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            BookshelfEntity bookshelfEntity = bookshelfRepository.findById(idBookshelf).get();
            BookshelfDTO bookshelfDTO = new BookshelfDTO();
            modelMapper.map(bookshelfEntity, bookshelfDTO);
            dataResponse.setData(bookshelfDTO);
            dataResponse.setMessage("Bookshelf found");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can not find Bookshelf");
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addBookShelf(BookShelfRequest bookShelfRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BookshelfEntity bookshelfEntity = new BookshelfEntity();
        modelMapper.map(bookShelfRequest, bookshelfEntity);
        bookshelfEntity.setStatus(Status.EMPTY);
        bookshelfEntity.setCurrentCapacity(bookShelfRequest.getCapacity());
        bookshelfRepository.save(bookshelfEntity);
        messageResponse.setMessage("Bookshelf added");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateBookShelf(BookShelfRequest bookShelfRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            BookshelfEntity bookshelfEntity = bookshelfRepository.findById(bookShelfRequest.getIdBookshelf()).get();
            modelMapper.map(bookShelfRequest, bookshelfEntity);
            if (bookShelfRequest.getCapacity() > bookshelfEntity.getCapacity()){
                Long currentCapacityNew = (bookShelfRequest.getCapacity() - bookshelfEntity.getCapacity()) + bookshelfEntity.getCurrentCapacity();
                bookshelfEntity.setCurrentCapacity(currentCapacityNew);
            } else if (bookShelfRequest.getCapacity() < bookshelfEntity.getCapacity()){
                Long currentCapacityNew = bookshelfEntity.getCurrentCapacity() - (bookshelfEntity.getCapacity() - bookShelfRequest.getCapacity());
                bookshelfEntity.setCurrentCapacity(currentCapacityNew);
            }
            bookshelfRepository.save(bookshelfEntity);
            messageResponse.setMessage("Bookshelf updated");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can not find Bookshelf");
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deleteBookShelf(Long idBookshelf) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            BookshelfEntity bookshelfEntity = bookshelfRepository.findById(idBookshelf).get();
            bookshelfRepository.delete(bookshelfEntity);
            messageResponse.setMessage("Bookshelf updated");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("Can not find Bookshelf");
            return messageResponse;
        }
    }


}
