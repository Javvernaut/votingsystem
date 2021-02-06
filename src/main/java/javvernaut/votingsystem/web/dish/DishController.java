package javvernaut.votingsystem.web.dish;

import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.util.exception.ForbiddenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.ValidationUtil.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(DishController.DISHES_URL)
public class DishController {

    public static final String DISHES_URL = "/api/admin/restaurants/{restaurantId}/dishes";
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping()
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return dishRepository.findAllByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(dishRepository.findByIdAndRestaurantId(id, restaurantId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(restaurantId), restaurantId);
        dish.setRestaurant(restaurant);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DISHES_URL + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Transactional
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("update {}", dish);
        assureIdConsistent(dish, id);
        checkNotFoundWithIdAndRestaurantId(id, restaurantId);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {}", id);
        Dish dish = checkNotFoundWithIdAndRestaurantId(id, restaurantId);
        if (!CollectionUtils.isEmpty(dish.getItems())) {
            throw new ForbiddenException("Dish id=" + id + " cannot be deleted. Delete it from menu first.");
        }
        checkSingleModification(dishRepository.deleteByIdAndRestaurantId(id, restaurantId), "Dish id=" + id + " missed");
    }

    private Dish checkNotFoundWithIdAndRestaurantId(int id, int restaurantId) {
        return checkNotFoundWithId(dishRepository.findByIdAndRestaurantId(id, restaurantId),
                "Dish id=" + id + " doesn't belong to restaurant id=" + restaurantId);
    }
}
