package javvernaut.votingsystem.web.menu;

import javvernaut.votingsystem.MenuTestData;
import javvernaut.votingsystem.model.Menu;
import javvernaut.votingsystem.repository.MenuRepository;
import javvernaut.votingsystem.util.JsonUtil;
import javvernaut.votingsystem.web.AbstractControllerTest;
import javvernaut.votingsystem.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static javvernaut.votingsystem.MenuTestData.*;
import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.TestUtil.readFromJson;
import static javvernaut.votingsystem.UserTestData.mockAdmin;
import static javvernaut.votingsystem.UserTestData.mockUser;
import static javvernaut.votingsystem.util.DateUtil.current_date;
import static javvernaut.votingsystem.web.menu.MenuController.MENUS_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    private static final String TEST_MENUS_URL = MENUS_URL + "/";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL, RESTAURANT3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu7, menu8, menu9));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL + MENU5_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu5));
    }

    @Test
    void getWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL + MENU3_ID, RESTAURANT3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL + NOT_FOUND_MENU_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL + MENU3_ID, RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_MENUS_URL + MENU6_ID, RESTAURANT3_ID)
                .with(getHttpBasic(mockUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_MENUS_URL, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(newMenu))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isCreated());
        Menu created = readFromJson(action, Menu.class);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.findByIdAndRestaurantId(newId, RESTAURANT2_ID).get(), newMenu);
    }

    @Test
    void createWrongDate() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        newMenu.setMenuDate(current_date);
        perform(MockMvcRequestBuilders.post(TEST_MENUS_URL, RESTAURANT3_ID)
                .content(JsonUtil.writeValue(newMenu))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_MENUS_URL + MENU6_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findByIdAndRestaurantId(MENU6_ID, RESTAURANT2_ID).isPresent());
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_MENUS_URL + MENU7_ID, RESTAURANT3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_MENUS_URL + MENU2_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(TEST_MENUS_URL + MENU6_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(menuRepository.findByIdAndRestaurantId(MENU6_ID, RESTAURANT2_ID).get(), updated);
    }

    @Test
    void updateWrongDate() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        updated.setMenuDate(current_date);
        perform(MockMvcRequestBuilders.put(TEST_MENUS_URL + MENU6_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateWrongRestaurant() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        updated.setMenuDate(current_date);
        perform(MockMvcRequestBuilders.put(TEST_MENUS_URL + MENU6_ID, RESTAURANT3_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalid() throws Exception {
        Menu newMenu = new Menu(null, null);
        perform(MockMvcRequestBuilders.post(TEST_MENUS_URL, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(newMenu))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        updated.setMenuDate(null);
        perform(MockMvcRequestBuilders.put(TEST_MENUS_URL + MENU6_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Menu newMenu = new Menu(null, LocalDate.of(2020, 12, 12));
        perform(MockMvcRequestBuilders.post(TEST_MENUS_URL, RESTAURANT3_ID)
                .content(JsonUtil.writeValue(newMenu))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU_DATE)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        updated.setMenuDate(menu5.getMenuDate());
        perform(MockMvcRequestBuilders.put(TEST_MENUS_URL + MENU6_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU_DATE)));

    }
}