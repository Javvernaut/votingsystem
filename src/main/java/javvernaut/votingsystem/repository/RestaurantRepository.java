package javvernaut.votingsystem.repository;

import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.jpa.RestaurantJpaRepository;

import java.util.List;

public class RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepository(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    public Restaurant get(int id) {
        return restaurantJpaRepository.findById(id).orElseThrow();
    }

    public List<Restaurant> getAll() {
        return restaurantJpaRepository.findAll();
    }

    public Restaurant save(Restaurant restaurant) {
        return restaurantJpaRepository.save(restaurant);
    }

    public void delete(int id) {
        restaurantJpaRepository.deleteById(id);
    }
}
