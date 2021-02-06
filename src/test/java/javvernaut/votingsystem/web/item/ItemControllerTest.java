package javvernaut.votingsystem.web.item;

import javvernaut.votingsystem.ItemTestData;
import javvernaut.votingsystem.repository.ItemRepository;
import javvernaut.votingsystem.to.ItemTo;
import javvernaut.votingsystem.util.ItemUtil;
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

import java.util.List;

import static javvernaut.votingsystem.DishTestData.*;
import static javvernaut.votingsystem.ItemTestData.*;
import static javvernaut.votingsystem.MenuTestData.*;
import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.TestUtil.readFromJson;
import static javvernaut.votingsystem.UserTestData.mockAdmin;
import static javvernaut.votingsystem.UserTestData.mockUser;
import static javvernaut.votingsystem.web.item.ItemController.ITEMS_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest extends AbstractControllerTest {
    private static final String TEST_ITEMS_URL = ITEMS_URL + "/";

    @Autowired
    ItemRepository itemRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL, RESTAURANT2_ID, MENU6_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ITEM_TO_MATCHER.contentJson(ItemUtil.getTos(List.of(item14, item15, item16))));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + ITEM6_ID, RESTAURANT1_ID, MENU2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ITEM_TO_MATCHER.contentJson(ItemUtil.createTo(item6)));
    }

    @Test
    void getWrongMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + ITEM6_ID, RESTAURANT1_ID, MENU3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + ITEM6_ID, RESTAURANT2_ID, MENU2_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + NOT_FOUND_ITEM_ID, RESTAURANT3_ID, MENU8_ID)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + ITEM23_ID, RESTAURANT3_ID, MENU8_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(TEST_ITEMS_URL + ITEM23_ID, RESTAURANT3_ID, MENU8_ID)
                .with(getHttpBasic(mockUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createWithLocation() throws Exception {
        ItemTo newItemTo = ItemTestData.getNewTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT2_ID, MENU5_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isCreated());
        ItemTo created = readFromJson(action, ItemTo.class);
        int newId = created.getId();
        newItemTo.setId(newId);
        newItemTo.setName(created.getName());
        ITEM_TO_MATCHER.assertMatch(created, newItemTo);
        ITEM_TO_MATCHER.assertMatch(ItemUtil.createTo(itemRepository.findByIdAndMenuIdAndMenuRestaurantId(newId, MENU5_ID, RESTAURANT2_ID).get()), newItemTo);
    }

    @Test
    void createWrongDate() throws Exception {
        ItemTo newItemTo = ItemTestData.getNewTo();
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT3_ID, MENU8_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void createWrongMenu() throws Exception {
        ItemTo newItemTo = ItemTestData.getNewTo();
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT2_ID, MENU9_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createWrongDish() throws Exception {
        ItemTo newItemTo = ItemTestData.getNewTo();
        newItemTo.setDishId(DISH10_ID);
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT2_ID, MENU5_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalidPrice() throws Exception {
        ItemTo newItemTo = ItemTestData.getNewTo();
        newItemTo.setPrice(null);
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT2_ID, MENU5_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ITEMS_URL + ITEM8_ID, RESTAURANT1_ID, MENU3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(itemRepository.findByIdAndMenuIdAndMenuRestaurantId(ITEM8_ID, MENU3_ID, RESTAURANT1_ID).isPresent());
    }

    @Test
    void deleteWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ITEMS_URL + ITEM8_ID, RESTAURANT2_ID, MENU3_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteWrongMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ITEMS_URL + ITEM11_ID, RESTAURANT2_ID, MENU7_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.delete(TEST_ITEMS_URL + ITEM11_ID, RESTAURANT2_ID, MENU4_ID)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updatePrice() throws Exception {
        ItemTo updated = new ItemTo(ITEM26_ID, dish13.getId(), dish13.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM26_ID, RESTAURANT3_ID, MENU9_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        ITEM_MATCHER.assertMatch(
                itemRepository.findByIdAndMenuIdAndMenuRestaurantId(ITEM26_ID, MENU9_ID, RESTAURANT3_ID).get(),
                ItemUtil.updatePriceFromTo(item26, updated));
    }

    @Test
    void updatePriceWrongItemId() throws Exception {
        ItemTo updated = new ItemTo(ITEM26_ID, dish13.getId(), dish13.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM21_ID, RESTAURANT3_ID, MENU9_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updatePriceWrongDish() throws Exception {
        ItemTo updated = new ItemTo(ITEM27_ID, dish14.getId(), dish14.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM27_ID, RESTAURANT3_ID, MENU8_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updatePriceWrongRestaurant() throws Exception {
        ItemTo updated = new ItemTo(ITEM27_ID, dish14.getId(), dish14.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM27_ID, RESTAURANT2_ID, MENU9_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updatePriceWrongDate() throws Exception {
        ItemTo updated = new ItemTo(ITEM21_ID, dish12.getId(), dish12.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM21_ID, RESTAURANT3_ID, MENU8_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updatePriceWrongMenu() throws Exception {
        ItemTo updated = new ItemTo(ITEM26_ID, dish14.getId(), dish14.getName(), 8888);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM26_ID, RESTAURANT3_ID, MENU7_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updatePriceWrong() throws Exception {
        ItemTo updated = new ItemTo(ITEM26_ID, dish13.getId(), dish13.getName(), null);
        perform(MockMvcRequestBuilders.patch(TEST_ITEMS_URL + ITEM26_ID, RESTAURANT3_ID, MENU9_ID)
                .content(JsonUtil.writeValue(updated))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }



    @Test
    void createInvalid() throws Exception {
        ItemTo newItemTo = new ItemTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT2_ID, MENU5_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        ItemTo newItemTo = new ItemTo(null, dish3.getId(), null, 2222);
        perform(MockMvcRequestBuilders.post(TEST_ITEMS_URL, RESTAURANT1_ID, MENU3_ID)
                .content(JsonUtil.writeValue(newItemTo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getHttpBasic(mockAdmin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_ITEM)));
    }
}