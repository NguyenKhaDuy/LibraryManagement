package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.LibraryCardEntity;
import org.example.librarymanagement.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {
    List<LibraryCardEntity> findByReadersEntity_IdReaderAndStatus(String idReader, Status status);

    List<LibraryCardEntity> findByReadersEntity_IdReader(String idReader);
}
