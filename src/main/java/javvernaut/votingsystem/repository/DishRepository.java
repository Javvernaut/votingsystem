package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:id ORDER BY d.name")
    List<Dish> findAllByRestaurantId(int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int deleteByIdAndRestaurantId(int id, int restaurantId);
}
