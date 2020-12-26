package javvernaut.votingsystem.repository.jpa;

import javvernaut.votingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantJpaRepository extends JpaRepository<Restaurant, Integer> {
}
