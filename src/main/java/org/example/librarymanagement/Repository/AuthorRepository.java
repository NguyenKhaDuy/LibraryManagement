package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<String, AuthorEntity> {
}
