package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.ImportBookDTO;
import org.example.librarymanagement.Model.Requests.ImportBookRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.ImportBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImportBookController {
    @Autowired
    ImportBookService importBookService;

    @GetMapping(value = "/api/staff/import/book")
    public ResponseEntity<Object> getAllImportBooks(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<ImportBookDTO> importBookDTOS = importBookService.getImportBooks(pageNo);
        dataResponse.setData(importBookDTOS.getContent());
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        dataResponse.setTotal_pages(importBookDTOS.getTotalPages());
        dataResponse.setCurrent_page(pageNo);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/staff/import/book/idImport={id}")
    public ResponseEntity<Object> getImportBookById(@PathVariable("id") String id) {
        Object result = importBookService.getImportBook(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/staff/import/book")
    public ResponseEntity<Object> addImportBook(@RequestBody ImportBookRequest importBookRequest) {
        MessageResponse messageResponse = importBookService.addImportBook(importBookRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/api/staff/import/book")
    public ResponseEntity<Object> updateImportBook(@RequestBody ImportBookRequest importBookRequest) {
        MessageResponse messageResponse = importBookService.updateImportBook(importBookRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/staff/import/book/idImport={id}")
    public ResponseEntity<Object> deleteImportBook(@PathVariable("id") String id) {
        MessageResponse messageResponse = importBookService.deleteImportBook(id);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
