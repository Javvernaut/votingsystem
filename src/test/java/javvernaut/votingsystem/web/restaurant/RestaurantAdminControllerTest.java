package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.RestaurantTestData;
import javvernaut.votingsystem.model.Restaurant;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.util.JsonUtil;
import javvernaut.votingsystem.util.exception.NotFoundException;
import javvernaut.votingsystem.web.AbstractControllerTest;
import javvernaut.votingsystem.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.TestUtil.readFromJson;
import static javvernaut.votingsystem.UserTestData.mockAdmin;
import static javvernaut.votingsystem.UserTestData.mockUser;
import static javvernaut.votingsystem.web.restaurant.RestaurantAdminController.ADMIN_RESTAURANTS_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantAdminControllerTest extends AbstractControllerTest {

    private static final String TEST_ADMIN_RESTAURANTS_URL = ADMIN_RESTAURANTS_URL + "/";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_RESTAURANTS_URL)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2, restaurant3, restaurant1));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_RESTAURANTS_URL + NOT_FOUND_RESTAURANT_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ADMIN_RESTAURANTS_URL)
                .with(getHttpBasic(mockUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_ADMIN_RESTAURANTS_URL)
                .content(JsonUtil.writeValue(newRestaurant))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isCreated());
        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getOne(newId), newRestaurant);

    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantRepository.getExisted(RESTAURANT3_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ADMIN_RESTAURANTS_URL + NOT_FOUND_RESTAURANT_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT2_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(RESTAURANT2_ID), getUpdated());
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(TEST_ADMIN_RESTAURANTS_URL)
                .content(JsonUtil.writeValue(invalid))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = getUpdated();
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT2_ID)
                .content(JsonUtil.writeValue(invalid))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDuplicate() throws Exception {
        Restaurant duplicate = getNew();
        duplicate.setName(restaurant1.getName());
        perform(MockMvcRequestBuilders.post(TEST_ADMIN_RESTAURANTS_URL)
                .content(JsonUtil.writeValue(duplicate))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_RESTAURANT_DUPLICATE_NAME)));

    }

    @Test
    void updateDuplicate() throws Exception {
        Restaurant duplicate = getUpdated();
        duplicate.setName(restaurant1.getName());
        perform(MockMvcRequestBuilders.put(TEST_ADMIN_RESTAURANTS_URL + RESTAURANT1_ID)
                .content(JsonUtil.writeValue(duplicate))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_RESTAURANT_DUPLICATE_NAME)));
    }
}