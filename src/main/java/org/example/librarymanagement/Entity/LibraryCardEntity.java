package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "library_card")
public class LibraryCardEntity {
    @Id
    @Column(name = "id_card")
    private String idCard;

    @Column(name = "date_of_issue")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateOfIssue;

    @Column(name = "expiration_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reader")
    private ReadersEntity readersEntity;
}
