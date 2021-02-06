package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static javvernaut.votingsystem.util.ValidationUtil.checkNew;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = AdminController.ADMIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminController extends AbstractUserController {
    public static final String ADMIN_URL = "/api/admin/users";

    @GetMapping
    public List<User> getAll() {
        log.info("get all");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping("/by")
    public ResponseEntity<User> getByMail(@RequestParam String email) {
        log.info("get by email={}", email);
        return ResponseEntity.of(repository.findByEmail(email));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = super.prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //TODO check votes
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) throws BindException {
        validateBeforeUpdate(user, id);
        log.info("update {}", user);
        prepareAndSave(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
    }
}
