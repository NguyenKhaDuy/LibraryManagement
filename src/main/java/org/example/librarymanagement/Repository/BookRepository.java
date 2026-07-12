package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BooksEntity, String> {
        @Query("""
    SELECT b
    FROM BooksEntity b
    WHERE (:author IS NULL OR b.authorEntity = :author)
      AND (:bookshelfDTO IS NULL OR b.bookshelfEntity = :bookshelfDTO)
      AND (:category IS NULL OR b.categoryEntity = :category)
      AND (:publishingHouse IS NULL OR b.publishingHouseEntity = :publishingHouse)
""")
    Page<BooksEntity> searchBooks(
            @Param("author") AuthorEntity author,
            @Param("bookshelfDTO") BookshelfEntity bookshelfDTO,
            @Param("category") CategoryEntity category,
            @Param("publishingHouse") PublishingHouseEntity publishingHouse,
            Pageable pageable
    );
}
