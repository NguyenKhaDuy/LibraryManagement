package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.Requests.AddStaffRequest;
import org.example.librarymanagement.Model.Requests.LoginRequest;
import org.example.librarymanagement.Model.Requests.RegisterRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    MessageResponse RegisterForUser(RegisterRequest registerRequest);
    MessageResponse AddStaff(AddStaffRequest addStaffRequest);
    Object Login(LoginRequest loginRequest);
}
