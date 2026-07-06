package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.BookshelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookshelfRepository extends JpaRepository<BookshelfEntity, Long> {
}
