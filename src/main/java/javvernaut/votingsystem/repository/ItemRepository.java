package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.dish d " +
            "JOIN FETCH i.menu m " +
            "WHERE i.menu.id=:menuId AND i.menu.restaurant.id=:restaurantId ORDER BY d.name")
    List<Item> findAllByMenuIdAndMenuRestaurantId(int menuId, int restaurantId);

    @Query("SELECT i FROM Item i " +
            "JOIN FETCH i.dish d " +
            "JOIN FETCH i.menu m " +
            "WHERE i.id=:id AND m.id=:menuId AND m.restaurant.id=:restaurantId")
    Optional<Item> findByIdAndMenuIdAndMenuRestaurantIdFetchAll(int id, int menuId, int restaurantId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.id=:id AND i.menu.id=:menuId AND i.menu.restaurant.id=:restaurantId")
    Optional<Item> findByIdAndMenuIdAndMenuRestaurantId(int id, int menuId, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Item i WHERE i=:item")
    int deleteByItem(Item item);
}
