package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.ReadersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepository extends JpaRepository<ReadersEntity, String> {
}
