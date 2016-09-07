package ru.jvdev.demoapp.server.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@RepositoryRestResource
public interface TaskRestRepository extends PagingAndSortingRepository<Task, Integer> {
}
