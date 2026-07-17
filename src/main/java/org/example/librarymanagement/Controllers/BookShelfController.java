package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.BookshelfDTO;
import org.example.librarymanagement.Model.Requests.BookShelfRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.BookShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookShelfController {
    @Autowired
    BookShelfService bookShelfService;

    @GetMapping(value = "/api/admin/book-shelf")
    public ResponseEntity<Object> getAllBookShelf(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<BookshelfDTO> bookshelfDTOS = bookShelfService.getAllBookShelf(pageNo);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(bookshelfDTOS.getContent());
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(bookshelfDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/book-shelfs")
    public ResponseEntity<Object> getAllBookShelf() {
        DataResponse dataResponse = bookShelfService.getAllBookShelf();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/book-shelf/idBookShelf={id}")
    public ResponseEntity<Object> getBookShelf(@PathVariable Long id) {
        Object result = bookShelfService.getBookShelfById(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/book-shelf")
    public ResponseEntity<Object> addBookShelf(@RequestBody BookShelfRequest bookShelfRequest) {
        MessageResponse messageResponse = bookShelfService.addBookShelf(bookShelfRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/book-shelf")
    public ResponseEntity<Object> updateBookShelf(@RequestBody BookShelfRequest bookShelfRequest) {
        MessageResponse messageResponse = bookShelfService.updateBookShelf(bookShelfRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/book-shelf/idBookShelf={id}")
    public ResponseEntity<Object> deleteBookShelf(@PathVariable Long id) {
        MessageResponse messageResponse = bookShelfService.deleteBookShelf(id);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
