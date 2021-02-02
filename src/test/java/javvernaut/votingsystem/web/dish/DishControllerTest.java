package javvernaut.votingsystem.web.dish;

import javvernaut.votingsystem.DishTestData;
import javvernaut.votingsystem.model.Dish;
import javvernaut.votingsystem.repository.DishRepository;
import javvernaut.votingsystem.util.JsonUtil;
import javvernaut.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static javvernaut.votingsystem.DishTestData.*;
import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.TestUtil.readFromJson;
import static javvernaut.votingsystem.UserTestData.mockAdmin;
import static javvernaut.votingsystem.UserTestData.mockUser;
import static javvernaut.votingsystem.web.dish.DishController.DISH_URL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {
    private static final String TEST_DISH_URL = DISH_URL + "/";

    @Autowired
    private DishRepository dishRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish7, dish8, dish9, dish6));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + DISH3_ID, RESTAURANT1_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish3));
    }

    @Test
    void getWrongRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + DISH14_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + NOT_FOUND_DISH_ID, RESTAURANT1_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + DISH6_ID, RESTAURANT2_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + DISH11_ID, RESTAURANT3_ID)
                .with(getHttpBasic(mockUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_DISH_URL + DISH13_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_DISH_URL, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(newDish))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isCreated());
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.findByIdAndRestaurantId(newId, RESTAURANT2_ID).get(), newDish);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_DISH_URL + DISH16_ID, RESTAURANT3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findByIdAndRestaurantId(DISH16_ID, RESTAURANT1_ID).isPresent());
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_DISH_URL + DISH8_ID, RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteWrong() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_DISH_URL + DISH13_ID, RESTAURANT1_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(TEST_DISH_URL + DISH12_ID, RESTAURANT3_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishRepository.findByIdAndRestaurantId(DISH12_ID, RESTAURANT3_ID).get(), updated);
    }

    @Test
    void updateWrongRestaurant() throws Exception {
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(TEST_DISH_URL + DISH12_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalid() throws Exception {
        Dish newDish = new Dish(null, null);
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_DISH_URL, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(newDish))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setName("");
        perform(MockMvcRequestBuilders.put(TEST_DISH_URL + DISH12_ID, RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Dish newDish = new Dish(null, "Chicken in ice cream");
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_DISH_URL, RESTAURANT3_ID)
                .content(JsonUtil.writeValue(newDish))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}