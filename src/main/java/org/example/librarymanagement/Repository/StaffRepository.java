package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity,String> {
}
