package org.example.librarymanagement.Model.Requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddStaffRequest extends RegisterRequest{
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateStart;
    private Double wage;
    private String position;
}
