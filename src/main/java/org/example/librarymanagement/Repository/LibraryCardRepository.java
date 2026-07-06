package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.LibraryCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {
}
