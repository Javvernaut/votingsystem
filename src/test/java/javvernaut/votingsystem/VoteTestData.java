package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Vote;

import java.time.LocalDate;

import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.UserTestData.*;
import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");

    public static final int VOTE1_ID = START_SEQ + 58;
    public static final int VOTE2_ID = START_SEQ + 59;
    public static final int VOTE3_ID = START_SEQ + 60;
    public static final int VOTE4_ID = START_SEQ + 61;
    public static final int VOTE5_ID = START_SEQ + 62;
    public static final int VOTE6_ID = START_SEQ + 63;
    public static final int VOTE7_ID = START_SEQ + 64;
    public static final int VOTE8_ID = START_SEQ + 65;
    public static final int VOTE9_ID = START_SEQ + 66;
    public static final int VOTE10_ID = START_SEQ + 67;

    public static final Vote vote1 = new Vote(VOTE1_ID, mockUser, restaurant2, LocalDate.of(2020, 12, 10));
    public static final Vote vote2 = new Vote(VOTE2_ID, mockUser, restaurant1, LocalDate.of(2020, 12, 11));
    public static final Vote vote3 = new Vote(VOTE3_ID, mockUser, restaurant2, LocalDate.of(2020, 12, 12));
    public static final Vote vote4 = new Vote(VOTE4_ID, mockUser, restaurant3, LocalDate.of(2020, 12, 9));
    public static final Vote vote5 = new Vote(VOTE5_ID, mockAdmin, restaurant1, LocalDate.of(2020, 12, 12));
    public static final Vote vote6 = new Vote(VOTE6_ID, mockAdmin, restaurant3, LocalDate.of(2020, 12, 10));
    public static final Vote vote7 = new Vote(VOTE7_ID, mockUser2, restaurant3, LocalDate.of(2020, 12, 9));
    public static final Vote vote8 = new Vote(VOTE8_ID, mockUser2, restaurant2, LocalDate.of(2020, 12, 10));
    public static final Vote vote9 = new Vote(VOTE9_ID, mockUser2, restaurant1, LocalDate.of(2020, 12, 11));
    public static final Vote vote10 = new Vote(VOTE10_ID, mockUser2, restaurant3, LocalDate.of(2020, 12, 12));
}
