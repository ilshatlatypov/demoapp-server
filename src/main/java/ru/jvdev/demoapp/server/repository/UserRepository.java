package ru.jvdev.demoapp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.User;

/**
 * Created by ilshat on 29.07.16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(@Param(USERNAME) String username);

    @RestResource(exported = false)
    @Modifying
    @Transactional
    @Query("update User u set u.password=:password where u.id=:id")
    void setPassword(@Param("id") int id, @Param("password") String password);
}
