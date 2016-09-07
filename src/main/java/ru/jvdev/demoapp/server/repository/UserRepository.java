package ru.jvdev.demoapp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.jvdev.demoapp.server.entity.User;

/**
 * Created by ilshat on 29.07.16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(@Param("username") String username);
}
