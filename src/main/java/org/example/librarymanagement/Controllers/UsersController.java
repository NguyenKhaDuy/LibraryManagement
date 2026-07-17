package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.ReaderDTO;
import org.example.librarymanagement.Model.DTO.StaffDTO;
import org.example.librarymanagement.Model.Requests.StaffRequest;
import org.example.librarymanagement.Model.Requests.UpdateAvatarRequest;
import org.example.librarymanagement.Model.Requests.UserRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    @Autowired
    UsersService usersService;

    @GetMapping(value = "/api/admin/reader")
    public ResponseEntity<Object> getAllReaders(@RequestParam(name = "pageNo", defaultValue = "1")  Integer pageNo) {
        Page<ReaderDTO> readerDTOS = usersService.getAllReaders(pageNo);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(readerDTOS.getContent());
        dataResponse.setMessage("Successfully retrieved list of readers");
        dataResponse.setTotal_pages(readerDTOS.getTotalPages());
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/staff")
    public ResponseEntity<Object> getAllStaff(@RequestParam(name = "pageNo", defaultValue = "1")  Integer pageNo) {
        Page<StaffDTO> staffDTOS = usersService.getAllStaff(pageNo);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(staffDTOS.getContent());
        dataResponse.setMessage("Successfully retrieved list of readers");
        dataResponse.setTotal_pages(staffDTOS.getTotalPages());
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/reader/idReader={idReader}")
    public ResponseEntity<Object> getReaderById(@PathVariable("idReader") String idReader) {
        Object result = usersService.getReaderById(idReader);
        if(result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/api/staff/idStaff={idStaff}")
    public ResponseEntity<Object> getStaffById(@PathVariable("idStaff") String idStaff) {
        Object result = usersService.getStaffById(idStaff);
        if(result instanceof MessageResponse) {
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/api/reader")
    public ResponseEntity<Object> updateReader(@RequestBody UserRequest userRequest) {
        MessageResponse messageResponse = usersService.updateReader(userRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/staff")
    public ResponseEntity<Object> updateStaff(@RequestBody StaffRequest staffRequest) {
        MessageResponse messageResponse = usersService.updateStaff(staffRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/reader/idReader={idReader}")
    public ResponseEntity<Object> deleteReaderById(@PathVariable("idReader") String idReader) {
        MessageResponse messageResponse = usersService.deleteReader(idReader);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/staff/idStaff={idStaff}")
    public ResponseEntity<Object> deleteStaffById(@PathVariable("idStaff") String idStaff) {
        MessageResponse messageResponse = usersService.deleteStaff(idStaff);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/reader/avatar")
    public ResponseEntity<Object> updateAvatarReader(@ModelAttribute UpdateAvatarRequest updateAvatarRequest) {
        MessageResponse messageResponse = usersService.updateAvatarReader(updateAvatarRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/staff/avatar")
    public ResponseEntity<Object> updateAvatarStaff(@ModelAttribute UpdateAvatarRequest updateAvatarRequest) {
        MessageResponse messageResponse = usersService.updateAvatarStaff(updateAvatarRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

}
