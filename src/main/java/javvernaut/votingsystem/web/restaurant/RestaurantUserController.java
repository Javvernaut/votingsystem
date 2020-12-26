package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.jpa.MenuRepository;
import javvernaut.votingsystem.repository.jpa.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Запросы обычных пользователей
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantUserController {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    //TODO Must Return restaurants with votes
    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public List<Dish> getMenu(@PathVariable int id) {
        return menuRepository.findClosestMenu(id);
    }
}
