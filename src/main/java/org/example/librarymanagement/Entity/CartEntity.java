package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCart;

    @OneToOne
    @JoinColumn(name = "id_reader")
    ReadersEntity readersEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_detail", joinColumns = @JoinColumn(name = "id_cart"), inverseJoinColumns = @JoinColumn(name = "id_book"))
    private List<BooksEntity> booksEntities = new ArrayList<>();
}
