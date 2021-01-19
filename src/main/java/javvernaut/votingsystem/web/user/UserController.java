package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.to.UserTo;
import javvernaut.votingsystem.util.UserUtil;
import javvernaut.votingsystem.util.security.AuthorizedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = UserController.PROFILE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController extends AbstractUserController {

    public static final String PROFILE_URL = "/api/profile";

    @GetMapping
    public ResponseEntity<User> get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        return super.get(authorizedUser.getId());
    }

    //TODO check votes
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        super.delete(authorizedUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("Register {}", userTo);
        User created = super.prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PROFILE_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        validateBeforeUpdate(userTo, authorizedUser.getId());
        User user = repository.getExisted(userTo.getId());
        log.info("update {}", user);
        super.prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}
