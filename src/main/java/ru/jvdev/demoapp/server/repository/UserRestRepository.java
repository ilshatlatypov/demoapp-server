package ru.jvdev.demoapp.server.repository;

import ru.jvdev.demoapp.server.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRestRepository extends CrudRepository<User, Integer> {
}