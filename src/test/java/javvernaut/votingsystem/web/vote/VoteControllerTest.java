package javvernaut.votingsystem.web.vote;

import javvernaut.votingsystem.TestUtil;
import javvernaut.votingsystem.model.Vote;
import javvernaut.votingsystem.repository.VoteRepository;
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

import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.TestUtil.getHttpBasic;
import static javvernaut.votingsystem.UserTestData.*;
import static javvernaut.votingsystem.VoteTestData.*;
import static javvernaut.votingsystem.util.DateUtil.current_date;
import static javvernaut.votingsystem.util.DateUtil.current_time;
import static javvernaut.votingsystem.web.vote.VoteController.VOTES_URL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_URL)
                .with(getHttpBasic(mockUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote3, vote2, vote1, vote4));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_URL + "/current")
                .with(getHttpBasic(mockUser2)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote8));
    }

    @Test
    void getNotFound() throws Exception {
        current_date = current_date.plusDays(1);
        perform(MockMvcRequestBuilders.get(VOTES_URL + "/current")
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNotFound());
        current_date = current_date.minusDays(1);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(VOTES_URL + "/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithLocation() throws Exception {
        current_date = current_date.plusDays(1);
        Vote newVote = new Vote(null, mockAdmin, restaurant2, LocalDate.of(2020, 12, 11));
        ResultActions action = perform(MockMvcRequestBuilders.post(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant2.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isCreated());
        Vote created = TestUtil.readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.findByUserIdAndDate(mockAdmin.id(), current_date).get(), newVote);
        current_date = current_date.minusDays(1);
    }

    @Test
    void createNoParam() throws Exception {
        current_date = current_date.plusDays(1);
        perform(MockMvcRequestBuilders.post(VOTES_URL)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isBadRequest());
        current_date = current_date.minusDays(1);
    }

    @Test
    void createWrongRestaurant() throws Exception {
        current_date = current_date.plusDays(1);
        perform(MockMvcRequestBuilders.post(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant3.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity());
        current_date = current_date.minusDays(1);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(VOTES_URL)
                .with(getHttpBasic(mockUser2)))
                .andExpect(status().isNoContent());
        assertFalse(voteRepository.findByUserIdAndDate(mockUser2.id(), current_date).isPresent());
    }

    @Test
    void deleteWrongTime() throws Exception {
        current_time = current_time.plusSeconds(1);
        perform(MockMvcRequestBuilders.delete(VOTES_URL)
                .with(getHttpBasic(mockUser2)))
                .andExpect(status().isUnprocessableEntity());
        current_time = current_time.minusSeconds(1);
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.patch(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isNoContent());
        assertEquals(voteRepository.findByUserIdAndDate(mockAdmin.id(), current_date).get().getRestaurant().id(), restaurant1.id());
    }

    @Test
    void updateWrongTime() throws Exception {
        current_time = current_time.plusSeconds(1);
        perform(MockMvcRequestBuilders.patch(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant1.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity());
        current_time = current_time.minusSeconds(1);
    }

    @Test
    void updateNoParam() throws Exception {
        perform(MockMvcRequestBuilders.patch(VOTES_URL)
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWrongRestaurant() throws Exception {
        current_date = current_date.plusDays(1);
        perform(MockMvcRequestBuilders.patch(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant3.id()))
                .with(getHttpBasic(mockUser2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        current_date = current_date.minusDays(1);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant3.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.patch(VOTES_URL)
                .param("restaurantId", String.valueOf(restaurant3.id()))
                .with(getHttpBasic(mockAdmin)))
                .andExpect(status().isUnprocessableEntity());
    }
}