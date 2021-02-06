package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Role;
import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.util.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {

    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "meals", "password", "votes");
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USER2_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final User mockUser = new User(USER_ID, "User", "user@ya.ru", "password", Role.USER);
    public static final User mockAdmin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.USER, Role.ADMIN);
    public static final User mockUser2 = new User(USER2_ID, "Alien", "al@mail.ru", "alien", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(mockUser2);
        updated.setName("newName");
        updated.setEmail("newmail@mail.org");
        updated.setEnabled(false);
        updated.setPassword("newPass");
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
