package ru.jvdev.demoapp.server.repository;

import ru.jvdev.demoapp.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ilshat on 29.07.16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
