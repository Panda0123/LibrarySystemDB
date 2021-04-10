package com.library.database_system.repository;

import com.library.database_system.domain.User;
import com.library.database_system.projections.UserProj;
import com.library.database_system.resulttransformer.UserTransformer;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, CustomUserRepository {
    List<UserTransformer> getUsersDetails();

    Optional<UserProj> findUserById(String id);

    @Query("FROM com.library.database_system.domain.User")
    Collection<UserProj> findAllUser();

    Optional<User> findById(String id);
}