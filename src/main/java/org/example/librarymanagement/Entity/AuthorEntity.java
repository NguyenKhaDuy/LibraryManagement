package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "author")
public class AuthorEntity {
    @Id
    @Column(name = "id_author")
    private String idAuthor;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Column(name = "home_town")
    private String homeTown;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "authorEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BooksEntity> booksEntities = new ArrayList<>();
}
