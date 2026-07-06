package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.BorrowTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicketEntity, String> {
}
