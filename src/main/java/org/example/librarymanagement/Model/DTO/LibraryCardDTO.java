package org.example.librarymanagement.Model.DTO;

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
public class LibraryCardDTO {
    private String idCard;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateOfIssue;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;
    private Status status;
    private Reader reader;
}
