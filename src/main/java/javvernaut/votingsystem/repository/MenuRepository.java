package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findAll();

    @EntityGraph(attributePaths = "restaurant", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m")
    List<Menu> getWithRestaurants();

    //@EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id")
    List<Menu> findAllByRestaurantId(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Optional<Menu> findByIdAndRestaurantId(int id, int restaurantId);

    @Query("SELECT m.dishes FROM Menu m WHERE m.restaurant.id=:restaurantId")
    List<Dish> findClosestMenu(int restaurantId);

    @Transactional
    @Modifying
    void deleteByIdAndRestaurantId(int id, int restaurantId);
}
