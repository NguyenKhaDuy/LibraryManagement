package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, String> {
    Page<AuthorEntity> findAll(Pageable pageable);
}
