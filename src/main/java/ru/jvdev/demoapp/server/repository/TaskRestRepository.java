package ru.jvdev.demoapp.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@RepositoryRestResource
public interface TaskRestRepository extends PagingAndSortingRepository<Task, Integer> {

    @RestResource(path = "allByUsername", rel = "byUsername")
    Page<Task> findByUserUsername(@Param(USERNAME) String username, Pageable p);

    @RestResource(path = "notDoneByUsername", rel = "notDoneByUsername")
    Page<Task> findByDoneFalseAndUserUsername(@Param(USERNAME) String username, Pageable p);

    @RestResource(path = "doneByUsername", rel = "doneByUsername")
    Page<Task> findByDoneTrueAndUserUsername(@Param(USERNAME) String username, Pageable p);
}
