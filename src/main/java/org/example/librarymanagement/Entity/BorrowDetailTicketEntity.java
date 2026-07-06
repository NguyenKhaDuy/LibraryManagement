package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "borrow_detail_ticket")
public class BorrowDetailTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBorrowDetailTicket;

    @Column(name = "status_borrow")
    private String statusBorrow;

    @Column(name = "status_return")
    private String statusReturn;

    @Column(name = "fine")
    private Double fine;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_borrow_ticket")
    private BorrowTicketEntity borrowTicketEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_book")
    private BooksEntity booksEntity;
}
