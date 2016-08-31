package ru.jvdev.demoapp.server.repository;

import ru.jvdev.demoapp.server.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ilshat on 04.08.16.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
