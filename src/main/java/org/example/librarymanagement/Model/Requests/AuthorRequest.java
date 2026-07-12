package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
    private String idAuthor;
    private String fullName;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String homeTown;
    private String nationality;
}
