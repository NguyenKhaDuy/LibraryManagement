package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BooksEntity, String> {
}
