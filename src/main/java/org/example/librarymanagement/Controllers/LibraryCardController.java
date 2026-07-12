package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.LibraryCardDTO;
import org.example.librarymanagement.Model.Requests.LibraryCardRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.LibraryCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryCardController {
    @Autowired
    LibraryCardService libraryCardService;

    @GetMapping(value = "/api/admin/library-card")
    public ResponseEntity<Object> getLibraryCards(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<LibraryCardDTO> libraryCardDTOS = libraryCardService.getAllLibraryCards(pageNo);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        dataResponse.setData(libraryCardDTOS.getContent());
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(libraryCardDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/library-card/idCard={idCard}")
    public ResponseEntity<Object> getLibraryCardById(@PathVariable("idCard") String idCard) {
        Object result = libraryCardService.getLibraryCard(idCard);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/staff/library-card")
    public ResponseEntity<Object> addLibraryCard(@RequestBody LibraryCardRequest libraryCardRequest) {
        MessageResponse messageResponse = libraryCardService.createCardReader(libraryCardRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/staff/library-card")
    public ResponseEntity<Object> updateLibraryCard(@RequestBody LibraryCardRequest libraryCardRequest) {
        MessageResponse messageResponse = libraryCardService.updateCardReader(libraryCardRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/staff/library-card/idCard={idCard}")
    public ResponseEntity<Object> deleteLibraryCard(@PathVariable("idCard") String idCard) {
        MessageResponse messageResponse = libraryCardService.deleteCardReader(idCard);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
