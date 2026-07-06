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
@Table(name = "books")
public class BooksEntity {
    @Id
    @Column(name = "id_book")
    private String idBook;

    @Column(name = "name_book")
    private String nameBook;

    @Column(name = "year_of_pub")
    private Long yearOfPub;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "edition")
    private Long edition;

    @Column(name = "language")
    private String language;

    @Column(name = "value_of_book")
    private Double valueOfBook;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "booksEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ImageEntity> imageEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_author")
    private AuthorEntity authorEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bookshelf")
    private BookshelfEntity bookshelfEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publising_house")
    private PublishingHouseEntity publishingHouseEntity;

    @OneToMany(mappedBy = "booksEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();

    @OneToMany(mappedBy = "booksEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImportBookDetailEntity> importBookDetailEntities = new ArrayList<>();
}
