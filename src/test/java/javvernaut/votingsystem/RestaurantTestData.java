package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Restaurant;

import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menus", "dishes", "votes");

    public static final int RESTAURANT1_ID = START_SEQ + 3;
    public static final int RESTAURANT2_ID = START_SEQ + 4;
    public static final int RESTAURANT3_ID = START_SEQ + 5;
    public static final int NOT_FOUND_RESTAURANT_ID = 10;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "McDonalds");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Burger King");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "KFC");

    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT2_ID, "Updated King");
    }
}
