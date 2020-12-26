package javvernaut.votingsystem.util.security;

import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.to.UserTo;
import javvernaut.votingsystem.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userTo= UserUtil.asTo(user);
    }

    public UserTo getUserTo() {
        return userTo;
    }

    public int getId() {
        return userTo.getId();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}
