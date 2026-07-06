package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "borrow_ticket")
public class BorrowTicketEntity {
    @Id
    @Column(name = "id_ticket")
    private String idTicket;

    @Column(name = "borrowing_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime borrowingDate;

    @Column(name = "due_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dueDate;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "total_fine")
    private Double totalFine;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reader")
    private ReadersEntity readersEntity;

    @OneToMany(mappedBy = "borrowTicketEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_staff")
    private StaffEntity staffEntity;
}
