package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Menu;

import java.time.LocalDate;

import static javvernaut.votingsystem.RestaurantTestData.*;
import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final TestMatcher<Menu> MENU_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Menu.class, "restaurant", "items");

    public static final int MENU1_ID = START_SEQ + 22;
    public static final int MENU2_ID = START_SEQ + 23;
    public static final int MENU3_ID = START_SEQ + 24;
    public static final int MENU4_ID = START_SEQ + 25;
    public static final int MENU5_ID = START_SEQ + 26;
    public static final int MENU6_ID = START_SEQ + 27;
    public static final int MENU7_ID = START_SEQ + 28;
    public static final int MENU8_ID = START_SEQ + 29;
    public static final int MENU9_ID = START_SEQ + 30;
    public static final int NOT_FOUND_MENU_ID = 10;

    public static final Menu menu1 = new Menu(MENU1_ID, LocalDate.of(2020, 12, 10));
    public static final Menu menu2 = new Menu(MENU2_ID, LocalDate.of(2020, 12, 11));
    public static final Menu menu3 = new Menu(MENU3_ID, LocalDate.of(2020, 12, 12));
    public static final Menu menu4 = new Menu(MENU4_ID, LocalDate.of(2020, 12, 10));
    public static final Menu menu5 = new Menu(MENU5_ID, LocalDate.of(2020, 12, 11));
    public static final Menu menu6 = new Menu(MENU6_ID, LocalDate.of(2020, 12, 12));
    public static final Menu menu7 = new Menu(MENU7_ID, LocalDate.of(2020, 12, 9));
    public static final Menu menu8 = new Menu(MENU8_ID, LocalDate.of(2020, 12, 10));
    public static final Menu menu9 = new Menu(MENU9_ID, LocalDate.of(2020, 12, 12));

    static {
        menu1.setRestaurant(restaurant1);
        menu2.setRestaurant(restaurant1);
        menu3.setRestaurant(restaurant1);
        menu4.setRestaurant(restaurant2);
        menu5.setRestaurant(restaurant2);
        menu6.setRestaurant(restaurant2);
        menu7.setRestaurant(restaurant3);
        menu8.setRestaurant(restaurant3);
        menu9.setRestaurant(restaurant3);
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2020, 12, 15));
    }

    public static Menu getUpdated() {
        return new Menu(MENU6_ID, LocalDate.of(2020, 12, 20));
    }
}
