package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.util.DateUtil;
import javvernaut.votingsystem.util.ItemUtil;
import javvernaut.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static javvernaut.votingsystem.ItemTestData.*;
import static javvernaut.votingsystem.MenuTestData.MENU_MATCHER;
import static javvernaut.votingsystem.MenuTestData.menu4;
import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.web.restaurant.RestaurantUserController.USER_RESTAURANTS_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantUserControllerTest extends AbstractControllerTest {

    @Test
    void getAllWithVotesCountForCurrentDay() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo3, restaurantTo1, restaurantTo2));
    }

    @Test
    void getForCurrentDay() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT2_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantTo2));
    }

    @Test
    void getWrongRestaurant() throws Exception {
        DateUtil.current_date = DateUtil.current_date.plusDays(1);
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT3_ID))
                .andExpect(status().isNotFound());
        DateUtil.current_date = DateUtil.current_date.minusDays(1);
    }

    @Test
    void getMenuForCurrentDate() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT2_ID + "/menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu4));
    }

    @Test
    void getWrongMenu() throws Exception {
        DateUtil.current_date = DateUtil.current_date.plusDays(1);
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT3_ID + "/menu"))
                .andExpect(status().isUnprocessableEntity());
        DateUtil.current_date = DateUtil.current_date.minusDays(1);
    }

    @Test
    void getMenuItemsForCurrentDate() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT3_ID + "/menu/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ITEM_TO_MATCHER.contentJson(ItemUtil.getTos(List.of(item22, item21, item24, item23))));
    }

    @Test
    void getWrongItems() throws Exception {
        DateUtil.current_date = DateUtil.current_date.plusDays(1);
        perform(MockMvcRequestBuilders.get(USER_RESTAURANTS_URL + "/" + RESTAURANT3_ID + "/menu/items"))
                .andExpect(status().isUnprocessableEntity());
        DateUtil.current_date = DateUtil.current_date.minusDays(1);
    }
}