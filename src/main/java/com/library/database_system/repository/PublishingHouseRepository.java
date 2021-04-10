package com.library.database_system.repository;

import com.library.database_system.domain.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishingHouseRepository extends JpaRepository<PublishingHouse, Long> {

    Optional<PublishingHouse> findByName(String name);

    Optional<PublishingHouse> findByNameAndAddress(String name, String address);
}
