package org.example.librarymanagement.Model.Requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicketDetailRequest {
    private Long idBorrowDetailTicket;
    private String statusBorrow;
    private String statusReturn;
    private Double fine;
    private String note;
    private String bookId;
}
