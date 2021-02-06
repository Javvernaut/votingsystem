package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = "restaurant", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m")
    List<Menu> getWithRestaurants();

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id ORDER BY m.menuDate")
    List<Menu> findAllByRestaurantId(@Param("id") int id);

    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Optional<Menu> findByIdAndRestaurantId(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(int id);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id AND m.menuDate=:date")
    Optional<Menu> findByRestaurantIdAndDate(int id, LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId AND m.menuDate=:date")
    Optional<Menu> findByIdAndRestaurantIdAndDate(int id, int restaurantId, LocalDate date);
}
