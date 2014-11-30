package org.rickosborne.api.badger.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends CrudRepository<CheckIn, Long> {

    public List<CheckIn> findByUserOrderByDateSubmittedDesc(User user, Pageable pageable);

}
