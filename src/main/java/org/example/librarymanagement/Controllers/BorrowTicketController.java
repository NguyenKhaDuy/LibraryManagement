package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.BorrowTicketDTO;
import org.example.librarymanagement.Model.Requests.AcceptRequest;
import org.example.librarymanagement.Model.Requests.BorrowTicketRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.BorrowTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BorrowTicketController {
    @Autowired
    BorrowTicketService borrowTicketService;

    @GetMapping(value = "/api/admin/borrow-ticket")
    public ResponseEntity<Object> getBorrowTicket(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<BorrowTicketDTO> borrowTicketDTOS = borrowTicketService.getAllBorrowTickets(pageNo);
        dataResponse.setData(borrowTicketDTOS.getContent());
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(borrowTicketDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/user/borrow-ticket/idReader={idReader}")
    public ResponseEntity<Object> getBorrowTicketIDReader(@PathVariable("idReader") String idReader) {
        Object result = borrowTicketService.getAllBorrowTicketsByReader(idReader);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/borrow-ticket/idTicket={id}")
    public ResponseEntity<Object> getBorrowTicketID(@PathVariable("id") String id) {
        Object result = borrowTicketService.getBorrowTicketById(id);
        if (result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/borrow-ticket")
    public ResponseEntity<Object> addBorrowTicket(@RequestBody BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = borrowTicketService.addBorrowTicket(borrowTicketRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping(value = "/api/reader/borrow-ticket")
    public ResponseEntity<Object> addBorrowTicketByReader(@RequestBody BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = borrowTicketService.addBorrowTicketByReader(borrowTicketRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/borrow-ticket")
    public ResponseEntity<Object> updateBorrowTicket(@RequestBody BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = borrowTicketService.updateBorrowTicket(borrowTicketRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/staff/borrow-ticket")
    public ResponseEntity<Object> updateStatusTicket(@RequestBody AcceptRequest acceptRequest){
        MessageResponse messageResponse = borrowTicketService.updateStatus(acceptRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/borrow-ticket/idTicket={idTicket}")
    public ResponseEntity<Object> deleteBorrowTicket(@PathVariable("idTicket") String idTicket) {
        MessageResponse messageResponse = borrowTicketService.deleteBorrowTicket(idTicket);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
