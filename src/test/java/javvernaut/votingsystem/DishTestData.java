package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Dish;

import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant", "items");

    public static final int DISH1_ID = START_SEQ + 6;
    public static final int DISH2_ID = START_SEQ + 7;
    public static final int DISH3_ID = START_SEQ + 8;
    public static final int DISH4_ID = START_SEQ + 9;
    public static final int DISH5_ID = START_SEQ + 10;
    public static final int DISH6_ID = START_SEQ + 11;
    public static final int DISH7_ID = START_SEQ + 12;
    public static final int DISH8_ID = START_SEQ + 13;
    public static final int DISH9_ID = START_SEQ + 14;
    public static final int DISH10_ID = START_SEQ + 15;
    public static final int DISH11_ID = START_SEQ + 16;
    public static final int DISH12_ID = START_SEQ + 17;
    public static final int DISH13_ID = START_SEQ + 18;
    public static final int DISH14_ID = START_SEQ + 19;
    public static final int DISH15_ID = START_SEQ + 20;
    public static final int DISH16_ID = START_SEQ + 21;
    public static final int NOT_FOUND_DISH_ID = 10;

    public static final Dish dish1 = new Dish(DISH1_ID, "Chicken");
    public static final Dish dish2 = new Dish(DISH2_ID, "Soup");
    public static final Dish dish3 = new Dish(DISH3_ID, "Turkey in pita");
    public static final Dish dish4 = new Dish(DISH4_ID, "Borscht");
    public static final Dish dish5 = new Dish(DISH5_ID, "Juice");
    public static final Dish dish6 = new Dish(DISH6_ID, "Pizza");
    public static final Dish dish7 = new Dish(DISH7_ID, "Beer");
    public static final Dish dish8 = new Dish(DISH8_ID, "BlackJack");
    public static final Dish dish9 = new Dish(DISH9_ID, "Courtesans");
    public static final Dish dish10 = new Dish(DISH10_ID, "Gingerbread with chili");
    public static final Dish dish11 = new Dish(DISH11_ID, "Apple in ducks");
    public static final Dish dish12 = new Dish(DISH12_ID, "Birch juice with pulp");
    public static final Dish dish13 = new Dish(DISH13_ID, "Chicken in ice cream");
    public static final Dish dish14 = new Dish(DISH14_ID, "Dumplings with hazelnuts");
    public static final Dish dish15 = new Dish(DISH15_ID, "Puree with kotletka");
    public static final Dish dish16 = new Dish(DISH16_ID, "Мыш кродеться");

    static {
        dish1.setRestaurant(restaurant1);
        dish2.setRestaurant(restaurant1);
        dish3.setRestaurant(restaurant1);
        dish4.setRestaurant(restaurant1);
        dish5.setRestaurant(restaurant1);
        dish6.setRestaurant(restaurant2);
        dish7.setRestaurant(restaurant2);
        dish8.setRestaurant(restaurant2);
        dish9.setRestaurant(restaurant2);
        dish10.setRestaurant(restaurant3);
        dish11.setRestaurant(restaurant3);
        dish12.setRestaurant(restaurant3);
        dish13.setRestaurant(restaurant3);
        dish14.setRestaurant(restaurant3);
        dish15.setRestaurant(restaurant3);
        dish16.setRestaurant(restaurant3);
    }

    public static Dish getNew() {
        return new Dish(null, "newDish");
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(DISH12_ID, "UpdatedDish");
        updated.setRestaurant(restaurant3);
        return updated;
    }

}
