package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.ValidationUtil.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = RestaurantAdminController.ADMIN_RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantAdminController {

    public static final String ADMIN_RESTAURANTS_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;
    private final UniqueNameValidator nameValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(nameValidator);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all");
        return repository.findAllByOrderByName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_RESTAURANTS_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(repository.findById(id), id);
        repository.save(restaurant);
    }

    //TODO check votes
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        checkSingleModification(repository.delete(id), "Restaurant id=" + id + " missed");
    }
}
