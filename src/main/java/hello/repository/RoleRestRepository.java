package hello.repository;

import hello.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ilshat on 29.07.16.
 */
@RepositoryRestResource
public interface RoleRestRepository extends CrudRepository<Role, Integer> {
}
