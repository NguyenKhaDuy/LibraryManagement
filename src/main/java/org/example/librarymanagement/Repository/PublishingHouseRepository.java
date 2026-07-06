package org.example.librarymanagement.Repository;

import org.example.librarymanagement.Entity.PublishingHouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingHouseRepository extends JpaRepository<PublishingHouseEntity, Long> {
}
