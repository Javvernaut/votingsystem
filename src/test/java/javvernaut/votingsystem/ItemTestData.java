package javvernaut.votingsystem;

import javvernaut.votingsystem.model.Item;
import javvernaut.votingsystem.to.ItemTo;

import static javvernaut.votingsystem.DishTestData.*;
import static javvernaut.votingsystem.MenuTestData.*;
import static javvernaut.votingsystem.model.AbstractBaseEntity.START_SEQ;

public class ItemTestData {
    public static final TestMatcher<Item> ITEM_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Item.class, "menu", "dish");
    public static final TestMatcher<ItemTo> ITEM_TO_MATCHER = TestMatcher.usingEqualsComparator(ItemTo.class);

    public static final int ITEM1_ID = START_SEQ + 31;
    public static final int ITEM2_ID = START_SEQ + 32;
    public static final int ITEM3_ID = START_SEQ + 33;
    public static final int ITEM4_ID = START_SEQ + 34;
    public static final int ITEM5_ID = START_SEQ + 35;
    public static final int ITEM6_ID = START_SEQ + 36;
    public static final int ITEM7_ID = START_SEQ + 37;
    public static final int ITEM8_ID = START_SEQ + 38;
    public static final int ITEM9_ID = START_SEQ + 39;
    public static final int ITEM10_ID = START_SEQ + 40;
    public static final int ITEM11_ID = START_SEQ + 41;
    public static final int ITEM12_ID = START_SEQ + 42;
    public static final int ITEM13_ID = START_SEQ + 43;
    public static final int ITEM14_ID = START_SEQ + 44;
    public static final int ITEM15_ID = START_SEQ + 45;
    public static final int ITEM16_ID = START_SEQ + 46;
    public static final int ITEM17_ID = START_SEQ + 47;
    public static final int ITEM18_ID = START_SEQ + 48;
    public static final int ITEM19_ID = START_SEQ + 49;
    public static final int ITEM20_ID = START_SEQ + 50;
    public static final int ITEM21_ID = START_SEQ + 51;
    public static final int ITEM22_ID = START_SEQ + 52;
    public static final int ITEM23_ID = START_SEQ + 53;
    public static final int ITEM24_ID = START_SEQ + 54;
    public static final int ITEM25_ID = START_SEQ + 55;
    public static final int ITEM26_ID = START_SEQ + 56;
    public static final int ITEM27_ID = START_SEQ + 57;
    public static final int NOT_FOUND_ITEM_ID = 10;

    public static final Item item1 = new Item(ITEM1_ID, menu1, dish1, 333);
    public static final Item item2 = new Item(ITEM2_ID, menu1, dish4, 444);
    public static final Item item3 = new Item(ITEM3_ID, menu1, dish3, 555);
    public static final Item item4 = new Item(ITEM4_ID, menu2, dish2, 11);
    public static final Item item5 = new Item(ITEM5_ID, menu2, dish3, 12);
    public static final Item item6 = new Item(ITEM6_ID, menu2, dish5, 13);
    public static final Item item7 = new Item(ITEM7_ID, menu3, dish1, 55);
    public static final Item item8 = new Item(ITEM8_ID, menu3, dish2, 66);
    public static final Item item9 = new Item(ITEM9_ID, menu3, dish3, 123);
    public static final Item item10 = new Item(ITEM10_ID, menu3, dish4, 321);
    public static final Item item11 = new Item(ITEM11_ID, menu4, dish6, 456);
    public static final Item item12 = new Item(ITEM12_ID, menu4, dish7, 654);
    public static final Item item13 = new Item(ITEM13_ID, menu5, dish9, 753);
    public static final Item item14 = new Item(ITEM14_ID, menu6, dish7, 325);
    public static final Item item15 = new Item(ITEM15_ID, menu6, dish8, 475);
    public static final Item item16 = new Item(ITEM16_ID, menu6, dish6, 325);
    public static final Item item17 = new Item(ITEM17_ID, menu7, dish13, 220);
    public static final Item item18 = new Item(ITEM18_ID, menu7, dish15, 269);
    public static final Item item19 = new Item(ITEM19_ID, menu7, dish10, 900);
    public static final Item item20 = new Item(ITEM20_ID, menu7, dish11, 825);
    public static final Item item21 = new Item(ITEM21_ID, menu8, dish12, 880);
    public static final Item item22 = new Item(ITEM22_ID, menu8, dish11, 888);
    public static final Item item23 = new Item(ITEM23_ID, menu8, dish15, 801);
    public static final Item item24 = new Item(ITEM24_ID, menu8, dish13, 808);
    public static final Item item25 = new Item(ITEM25_ID, menu9, dish12, 330);
    public static final Item item26 = new Item(ITEM26_ID, menu9, dish13, 22);
    public static final Item item27 = new Item(ITEM27_ID, menu9, dish14, 77);

    public static ItemTo getNewTo() {
        return new ItemTo(null, dish6.getId(), dish6.getName(), 3333);
    }
}
