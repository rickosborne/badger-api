package org.rickosborne.api.badger.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long id);
    List<User> findByNameFirstContainingOrNameLastContainingOrEmailContaining(String firstLike, String lastLike, String emailLike);

}
