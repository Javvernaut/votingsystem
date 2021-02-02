package javvernaut.votingsystem.web.user;

import javvernaut.votingsystem.UserTestData;
import javvernaut.votingsystem.model.Role;
import javvernaut.votingsystem.model.User;
import javvernaut.votingsystem.repository.UserRepository;
import javvernaut.votingsystem.util.exception.NotFoundException;
import javvernaut.votingsystem.web.AbstractControllerTest;
import javvernaut.votingsystem.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.TestUtil.readFromJson;
import static javvernaut.votingsystem.UserTestData.*;
import static javvernaut.votingsystem.web.user.AdminController.ADMIN_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends AbstractControllerTest {

    private static final String TEST_ADMIN_URL = ADMIN_URL + "/";

    @Autowired
    UserRepository userRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(mockAdmin, mockUser2, mockUser));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL + USER2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(mockUser2));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL + NOT_FOUND)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL + USER_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL + USER_ID)
                .with(getHttpBasic(mockUser2)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_URL + "by?email=" + mockUser2.getEmail())
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(mockUser2));
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = UserTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());
        User created = readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getExisted(newId), newUser);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ADMIN_URL + USER_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userRepository.getExisted(USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ADMIN_URL + NOT_FOUND)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_URL + USER2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(updated, "newPass")))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.getExisted(USER2_ID), getUpdated());

    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(TEST_ADMIN_URL + USER2_ID)
                .param("enabled", "false")
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNoContent());
        assertFalse(userRepository.getExisted(USER2_ID).isEnabled());
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(TEST_ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        User invalid = new User(mockUser2);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_URL + mockUser2)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User duplicate = new User(null, "New", "user@ya.ru", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(TEST_ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(duplicate, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User duplicate = new User(mockUser);
        duplicate.setEmail(mockAdmin.getEmail());
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin))
                .content(jsonWithPassword(duplicate, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}