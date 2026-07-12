package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String idUser;
    private String fullName;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String gender;
    private String phone;
    private String email;
    private String cccd;
    private String address;
    private Status status;
}
