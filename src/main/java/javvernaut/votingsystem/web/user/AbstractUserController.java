package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.HasId;
import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.repository.UserRepository;
import javvernaut.votingsystem.util.UserUtil;
import javvernaut.votingsystem.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static javvernaut.votingsystem.util.ValidationUtil.assureIdConsistent;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    private LocalValidatorFactoryBean defaultValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

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

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, defaultValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
