package javvernaut.votingsystem.web.dish;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.util.exception.ForbiddenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.ValidationUtil.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(DishController.RESTAURANT_URL)
public class DishController {

    public static final String RESTAURANT_URL = "admin/restaurants/{restaurantId}";
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/dishes")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return dishRepository.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(dishRepository.findByIdAndRestaurantId(id, restaurantId));
    }

    @PostMapping(value = "/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResorce = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(RESTAURANT_URL + "/dishes/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResorce).body(created);
    }

    @PutMapping(value = "/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("update {}", dish);
        assureIdConsistent(dish, id);
        checkNotFoundWithId(
                dishRepository.findByIdAndRestaurantId(id, restaurantId),
                "Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @DeleteMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        Dish dish = checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(id, restaurantId),
                "Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        if (!dish.getItems().isEmpty()) {
            throw new ForbiddenException("Dish id=" + id + " cannot be deleted. Delete it from menu first.");
        }
        checkSingleModification(dishRepository.deleteByIdAndRestaurantId(id, restaurantId),
                "Dish id=" + id + ", restaurant id=" + restaurantId + " missed");
    }
}
