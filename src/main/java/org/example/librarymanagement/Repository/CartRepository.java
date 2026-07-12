package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.CartEntity;
import org.example.librarymanagement.Entity.ReadersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findByReadersEntity(ReadersEntity readersEntity);
}
