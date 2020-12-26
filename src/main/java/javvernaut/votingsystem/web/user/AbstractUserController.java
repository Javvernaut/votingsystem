package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.service.UserService;
import javvernaut.votingsystem.to.UserTo;
import javvernaut.votingsystem.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static javvernaut.votingsystem.util.ValidationUtil.assureIdConsistent;
import static javvernaut.votingsystem.util.ValidationUtil.checkNew;

public class AbstractUserController {

    @Autowired
    private UserService userService;

    protected List<User> getAll() {
        return userService.getAll();
    }

    protected User get(int id) {
        return userService.get(id);
    }

    protected User create(User user) {
        checkNew(user);
        return userService.create(user);
    }

    protected void delete(int id) {
        userService.delete(id);
    }

    protected void update(User user, int id) {
        assureIdConsistent(user, id);
        userService.update(user);
    }

    public void update(UserTo userTo, int id) {
        assureIdConsistent(userTo, id);
        User user = userService.get(userTo.getId());
        userService.update(UserUtil.updateFromTo(user, userTo));
    }

    protected User getByMail(String email) {
        return userService.getByEmail(email);
    }

    protected void enable(int id, boolean enabled) {
        userService.enable(id, enabled);
    }

    protected User create(UserTo userTo) {
        return userService.create(UserUtil.createNewFromTo(userTo));
    }
}
