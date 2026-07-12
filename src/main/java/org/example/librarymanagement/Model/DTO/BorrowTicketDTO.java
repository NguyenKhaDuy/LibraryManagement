package org.example.librarymanagement.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicketDTO {
    private String idTicket;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime borrowingDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dueDate;
    private Long quantity;
    private Double totalFine;
    private Status status;
    private String note;
    private Reader reader;
    private Staff staff;
    private List<BorrowDetailTicketDTO> borrowDetailTicketDTOS;
}
