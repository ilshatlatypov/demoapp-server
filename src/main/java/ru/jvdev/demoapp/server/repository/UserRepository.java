package ru.jvdev.demoapp.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.User;

/**
 * Created by ilshat on 29.07.16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(@Param(USERNAME) String username);

//    TODO use this search query on databases, where LIKE is case insensitive for cyrillic symbols
//    @RestResource(path = "findByNamesStartingWith", rel = "findByNamesStartingWith")
//    Page<User> findByFirstnameStartingWithOrLastnameStartingWith(@Param("firstname") String firstname,
//                                                                 @Param("lastname") String lastname,
//                                                                 Pageable p);

    // search query for sqlite3, where LIKE is case sensitive for cyrillic symbols
    @RestResource(path = "findByNamesStartingWith", rel = "findByNamesStartingWith")
    Page<User> findByFirstnameLowercaseStartingWithOrLastnameLowercaseStartingWith(@Param("firstname") String firstname,
                                                                                   @Param("lastname") String lastname,
                                                                                   Pageable p);
}
