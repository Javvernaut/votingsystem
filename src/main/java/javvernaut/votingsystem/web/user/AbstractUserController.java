package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.HasId;
import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.repository.jpa.UserRepository;
import javvernaut.votingsystem.util.UserUtil;
import javvernaut.votingsystem.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static javvernaut.votingsystem.util.ValidationUtil.assureIdConsistent;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    protected ResponseEntity<User> get(int id) {
        log.info("Get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }

    protected void delete(int id) {
        log.info("delete {}", id);
        ValidationUtil.checkSingleModification(repository.delete(id), id + " not found");
    }

    protected void validateBeforeUpdate(HasId user, int id) {
        assureIdConsistent(user, id);
    }
}
