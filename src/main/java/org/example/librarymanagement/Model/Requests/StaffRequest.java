package org.example.librarymanagement.Model.Requests;

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
public class StaffRequest extends UserRequest {
    private Double wage;
    private String position;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateStart;
}
