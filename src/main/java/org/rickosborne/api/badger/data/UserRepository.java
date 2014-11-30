package org.rickosborne.api.badger.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findAll();
    public User findById(Long id);
//    public User findByUserId(Long id);
    public List<User> findByNameFirstContainingOrNameLastContainingOrEmailContaining(String firstLike, String lastLike, String emailLike);

}
