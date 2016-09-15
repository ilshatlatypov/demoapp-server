package ru.jvdev.demoapp.server.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.User;

@RepositoryRestResource
public interface UserRestRepository extends PagingAndSortingRepository<User, Integer> {
    User findByUsername(@Param(USERNAME) String username);
}