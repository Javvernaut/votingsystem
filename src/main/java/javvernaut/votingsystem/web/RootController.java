package javvernaut.votingsystem.web;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootController {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants that presented menu for current date");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public List<Dish> getMenu(@PathVariable int id) {
        return null;
    }
}
