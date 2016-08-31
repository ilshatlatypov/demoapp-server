package ru.jvdev.demoapp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
//    @Modifying
//    @Query("update Task t set t.user=:user where t.id in :taskIds")
//    void setUserIdForTasks(@Param("user") User user, @Param("taskIds") Iterable<Integer> taskIds);
}
