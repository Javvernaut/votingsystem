package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    List<Dish> findAllByRestaurantId(int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int deleteByIdAndRestaurantId(int id, int restaurantId);
}
