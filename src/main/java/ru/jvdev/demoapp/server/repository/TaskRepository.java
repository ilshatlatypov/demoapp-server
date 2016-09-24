package ru.jvdev.demoapp.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Task;

/**
 * Created by ilshat on 04.08.16.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @RestResource(path = "allByUsername", rel = "allByUsername")
    Page<Task> findByUserUsername(@Param(USERNAME) String username, Pageable p);

    @RestResource(path = "notDone", rel = "notDone")
    Page<Task> findByDoneFalse(Pageable p);

    @RestResource(path = "done", rel = "done")
    Page<Task> findByDoneTrue(Pageable p);

    @RestResource(path = "notDoneByUsername", rel = "notDoneByUsername")
    Page<Task> findByDoneFalseAndUserUsername(@Param(USERNAME) String username, Pageable p);

    @RestResource(path = "doneByUsername", rel = "doneByUsername")
    Page<Task> findByDoneTrueAndUserUsername(@Param(USERNAME) String username, Pageable p);

    @RestResource(exported = false)
    @Modifying
    @Transactional
    @Query("update Task t set t.user.id=:userId where t.id in :taskIds")
    void setUserIdForTasks(@Param("userId") int userId, @Param("taskIds") Iterable<Integer> taskIds);
}
