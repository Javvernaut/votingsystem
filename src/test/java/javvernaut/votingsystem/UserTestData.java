package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Role;
import javvernaut.votingsystem.model.User;

import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {

    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "meals", "password");
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final User mockUser = new User(USER_ID, "User", "user@ya.ru", "password", Role.USER);
    public static final User mockAdmin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.USER, Role.ADMIN);
}
