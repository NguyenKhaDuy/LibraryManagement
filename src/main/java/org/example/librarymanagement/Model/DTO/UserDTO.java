package org.example.librarymanagement.Model.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
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
    private String avatar;
}
