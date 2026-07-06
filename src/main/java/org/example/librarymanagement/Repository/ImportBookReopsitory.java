package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.ImportBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportBookReopsitory extends JpaRepository<ImportBookEntity, String> {
}
