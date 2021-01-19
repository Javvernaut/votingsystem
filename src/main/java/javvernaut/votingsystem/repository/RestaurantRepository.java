package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT r FROM Restaurant r INNER JOIN r.menus m WHERE r.id=:id AND m.date=:date")
    Optional<Restaurant> findByIdAndMenuDate(int id, LocalDate date);

/*    @Query("SELECT new javvernaut.votingsystem.to.RestaurantTo(r.id, r.name, COUNT (v)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "INNER JOIN r.menus m " +
            "WHERE m.date=:date " +
            "GROUP BY r ORDER BY COUNT(v) DESC ")
    List<RestaurantTo> findAllTosWithVotesByMenuDate(LocalDate date);*/

    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r INNER JOIN r.menus m WHERE m.date=:date")
    List<Restaurant> findAllTosWithVotesByMenuDate(LocalDate date);

/*    @Query("SELECT new javvernaut.votingsystem.to.RestaurantTo(r.id, r.name, COUNT (v)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "INNER JOIN r.menus m " +
            "WHERE r.id=:id AND m.date=:currentDate " +
            "GROUP BY r")
    Optional<RestaurantTo> findToByIdAndMenuDate(int id, LocalDate date);*/

    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r INNER JOIN r.menus m WHERE r.id=:id AND m.date=:date")
    Optional<Restaurant> findWithVotesByIdAndMenuDate(int id, LocalDate date);
}
