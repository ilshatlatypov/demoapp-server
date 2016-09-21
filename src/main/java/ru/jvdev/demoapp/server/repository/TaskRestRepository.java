package ru.jvdev.demoapp.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@RepositoryRestResource
public interface TaskRestRepository extends PagingAndSortingRepository<Task, Integer> {
    Page<Task> findByUserUsername(@Param(USERNAME) String username, Pageable p);
}
