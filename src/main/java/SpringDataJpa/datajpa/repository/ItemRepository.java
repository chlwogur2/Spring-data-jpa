package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
