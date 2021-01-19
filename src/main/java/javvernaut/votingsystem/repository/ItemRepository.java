package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Item;
import javvernaut.votingsystem.model.ItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, ItemId> {

    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.dish " +
            "WHERE i.menu.id=:menuId AND i.menu.restaurant.id=:restaurantId")
    List<Item> findAllByMenuIdAndMenuRestaurantId(int menuId, int restaurantId);

    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.dish d " +
            "JOIN FETCH i.menu m " +
            "WHERE d.id=:id AND m.id=:menuId AND m.restaurant.id=:restaurantId")
    Optional<Item> findByDishIdAndMenuIdAndMenuRestaurantId(int id, int menuId, int restaurantId);

    @Query("SELECT i FROM Item i JOIN FETCH i.dish d INNER JOIN i.menu m WHERE m.restaurant.id=:id AND m.date=:date")
    List<Item> findAllByRestaurantIdAndMenuDate(Integer id, LocalDate date);
}
