package javvernaut.votingsystem.web.dish;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.util.exception.ForbiddenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
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
@RequestMapping(DishAdminController.RESTAURANT_URL)
public class DishAdminController {

    public static final String RESTAURANT_URL = "/restaurants/{restaurantId}";
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

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

    @PostMapping("/dishes")
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

    @PutMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("update {}", dish);
        assureIdConsistent(dish, id);
        Dish existed = checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(id ,restaurantId), "Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        existed.setName(dish.getName());
        existed.setPrice(dish.getPrice());
    }

    @DeleteMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        Dish dish = checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(id, restaurantId), "Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId);
        if (!dish.getMenus().isEmpty()) {
            throw new ForbiddenException("Dish id=" + id + " cannot be deleted. It is present in menu.");
        }
        checkSingleModification(dishRepository.deleteByIdAndRestaurantId(id, restaurantId), "Dish id=" + id + ", restaurant id=" + restaurantId + " missed");
    }
}
