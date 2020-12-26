package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.repository.jpa.DishJpaRepository;
import javvernaut.votingsystem.repository.jpa.RestaurantJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DishRepository {

    private final DishJpaRepository dishJpaRepository;
    private final RestaurantJpaRepository restaurantJpaRepository;

    public DishRepository(DishJpaRepository dishJpaRepository, RestaurantJpaRepository restaurantJpaRepository) {
        this.dishJpaRepository = dishJpaRepository;
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    public Dish get(int id, int restaurantId) {
        return dishJpaRepository.findByIdAndRestaurantId(id, restaurantId).orElseThrow();
    }

    public List<Dish> getAll(int restaurantId) {
        return dishJpaRepository.findAllByRestaurantId(restaurantId);
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && dishJpaRepository.findByIdAndRestaurantId(dish.getId(), restaurantId).isEmpty()) {
            return null;
        }
        dish.setRestaurant(restaurantJpaRepository.getOne(restaurantId));
        return dishJpaRepository.save(dish);
    }

/*    @Transactional
    public void delete(int id, int restaurantId) {
        Dish dish = dishJpaRepository.findOneByIdAndRestaurantId(id, restaurantId).orElseThrow();
        dish.setActive(false);
    }*/


}
