package javvernaut.votingsystem.repository.jpa;

import javvernaut.votingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishJpaRepository extends JpaRepository<Dish, Integer> {
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    List<Dish> findAllByRestaurantId(int restaurantId);
}
