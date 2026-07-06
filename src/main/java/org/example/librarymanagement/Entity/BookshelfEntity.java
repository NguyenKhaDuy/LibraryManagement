package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_shelf")
public class BookshelfEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBookshelf;

    @Column(name = "location")
    private String location;

    @Column(name = "floor")
    private Long floor;

    @Column(name = "area")
    private String area;

    @Column(name = "capacity")
    private Long capacity;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "bookshelfEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BooksEntity> booksEntities = new ArrayList<>();
}
