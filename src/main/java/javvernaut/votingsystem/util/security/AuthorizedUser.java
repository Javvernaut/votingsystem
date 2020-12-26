package javvernaut.votingsystem.util.security;

import javvernaut.votingsystem.model.User;
import lombok.NonNull;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthorizedUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int getId() {
        return user.getId();
    }
}
