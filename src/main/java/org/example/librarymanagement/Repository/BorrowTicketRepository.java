package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.BorrowTicketEntity;
import org.example.librarymanagement.Entity.ReadersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicketEntity, String> {
    List<BorrowTicketEntity> findByReadersEntity(ReadersEntity readersEntity);
}
