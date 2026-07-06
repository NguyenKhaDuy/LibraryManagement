package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.BorrowDetailTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowDetailTicketRepository extends JpaRepository<BorrowDetailTicketEntity, Long> {
}
