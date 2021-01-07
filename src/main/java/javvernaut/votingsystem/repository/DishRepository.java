package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Dish;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    List<Dish> findAllByRestaurantId(int restaurantId);
}
