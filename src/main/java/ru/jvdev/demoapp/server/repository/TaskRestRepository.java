package ru.jvdev.demoapp.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@RepositoryRestResource
public interface TaskRestRepository extends CrudRepository<Task, Integer> {
}
