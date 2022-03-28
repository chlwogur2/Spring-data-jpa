package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
