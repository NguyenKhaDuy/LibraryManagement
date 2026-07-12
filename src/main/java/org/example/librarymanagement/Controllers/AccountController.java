package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.Requests.AddStaffRequest;
import org.example.librarymanagement.Model.Requests.LoginRequest;
import org.example.librarymanagement.Model.Requests.RegisterRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping(value = "/api/register")
    public ResponseEntity<Object> resgterForUser(@RequestBody RegisterRequest registerRequest){
        MessageResponse messageResponse = accountService.RegisterForUser(registerRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping(value = "/api/admin/register/staff")
    public ResponseEntity<Object> resgterForStaff(@RequestBody AddStaffRequest addStaffRequest){
        MessageResponse messageResponse = accountService.AddStaff(addStaffRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PostMapping(value = "/api/login")
    public ResponseEntity<Object> Login(@RequestBody LoginRequest loginRequest){
        Object result = accountService.Login(loginRequest);
        if (result instanceof MessageResponse){
            return new ResponseEntity<>((MessageResponse)result, ((MessageResponse)result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
