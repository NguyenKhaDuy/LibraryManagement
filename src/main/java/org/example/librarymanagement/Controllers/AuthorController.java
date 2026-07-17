package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.AuthorDTO;
import org.example.librarymanagement.Model.Requests.AuthorRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.AuthorRepository;
import org.example.librarymanagement.Services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorController {
    @Autowired
    AuthorService authorService;

    @GetMapping(value = "/api/admin/author")
    public ResponseEntity<Object> getAllAuthors(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<AuthorDTO> authorDTOS = authorService.getAllAuthors(pageNo);
        dataResponse.setData(authorDTOS.getContent());
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(authorDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/authors")
    public ResponseEntity<Object> getAllAuthors() {
        DataResponse dataResponse = authorService.getAllAuthors();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/author/id-author={idauthor}")
    public ResponseEntity<Object> getAuthorById(@PathVariable("idauthor") String idauthor) {
        Object result = authorService.getAuthorById(idauthor);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/author")
    public ResponseEntity<Object> addAuthor(@RequestBody AuthorRequest authorRequest) {
        MessageResponse messageResponse = authorService.addAuthor(authorRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/api/admin/author")
    public ResponseEntity<Object> updateAuthor(@RequestBody AuthorRequest authorRequest) {
        MessageResponse messageResponse = authorService.updateAuthor(authorRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/admin/author/id-author={idauthor}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable("idauthor") String idauthor) {
        MessageResponse messageResponse = authorService.deleteAuthor(idauthor);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
