package ghorned.timecapture.repository;

import ghorned.timecapture.entity.Punch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PunchRepository extends CrudRepository<Punch, Integer> {}
