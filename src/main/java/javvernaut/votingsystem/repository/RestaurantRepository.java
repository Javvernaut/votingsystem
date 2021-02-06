package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.to.RestaurantTo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r INNER JOIN r.menus m WHERE r.id=:id AND m.menuDate=:date")
    Optional<Restaurant> findByIdAndMenuDate(int id, LocalDate date);

    @Query("SELECT new javvernaut.votingsystem.to.RestaurantTo(r.id, r.name, COUNT (v)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "INNER JOIN r.menus m " +
            "WHERE m.menuDate=:date " +
            "GROUP BY r ORDER BY COUNT(v) DESC ")
    List<RestaurantTo> findAllTosWithVotesByMenuDate(LocalDate date);

    @Query("SELECT new javvernaut.votingsystem.to.RestaurantTo(r.id, r.name, COUNT (v)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "INNER JOIN r.menus m " +
            "WHERE r.id=:id AND m.menuDate=:date " +
            "GROUP BY r")
    Optional<RestaurantTo> findToByIdAndMenuDate(int id, LocalDate date);

    List<Restaurant> findAllByOrderByName();

    Optional<Restaurant> findByNameIgnoreCase(String name);
}
