package ru.jvdev.demoapp.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@RepositoryRestResource
@PreAuthorize("hasAuthority('MANAGER')")
public interface TaskRestRepository extends CrudRepository<Task, Integer> {
}
