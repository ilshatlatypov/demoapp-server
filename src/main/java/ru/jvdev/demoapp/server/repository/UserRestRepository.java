package ru.jvdev.demoapp.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.jvdev.demoapp.server.entity.User;

@RepositoryRestResource
public interface UserRestRepository extends CrudRepository<User, Integer> {
    @PreAuthorize("hasAuthority('MANAGER') or #username == authentication?.name")
    User findByUsername(@Param("username") String username);
}