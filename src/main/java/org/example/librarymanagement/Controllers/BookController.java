package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.BooksDTO;
import org.example.librarymanagement.Model.Requests.BookRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    @Autowired
    BooksService booksService;

    @GetMapping(value = "/api/book")
    public ResponseEntity<Object> getBooks(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "idAuthor", required = false) String idAuthor,
            @RequestParam(name =  "idCategory", required = false) Long idCategory,
            @RequestParam(name = "idBookShelf", required = false) Long idBookShelf,
            @RequestParam(name = "idPublisher", required = false) Long idPublisher
    ) {
        DataResponse dataResponse = new DataResponse();
        Page<BooksDTO> booksDTOS = booksService.getBooks(idAuthor, idCategory, idBookShelf, idPublisher, pageNo);
        if (booksDTOS == null) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("No books found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
        }
        dataResponse.setData(booksDTOS.getContent());
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(booksDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/book/id-book={id}")
    public ResponseEntity<Object> getBookById(@PathVariable("id") String id) {
        Object result = booksService.getBooksById(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/book")
    public ResponseEntity<Object> addBook(@RequestBody BookRequest bookRequest) {
        MessageResponse messageResponse = booksService.addBook(bookRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/admin/book")
    public ResponseEntity<Object> updateBook(@RequestBody BookRequest bookRequest) {
        MessageResponse messageResponse = booksService.updateBook(bookRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/admin/book/id-book={id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") String id) {
        MessageResponse messageResponse = booksService.deleteBook(id);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
