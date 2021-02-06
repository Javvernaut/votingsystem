package javvernaut.votingsystem.util;

import javvernaut.votingsystem.model.Item;
import javvernaut.votingsystem.to.ItemTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemUtil {

    public static Item updatePriceFromTo(Item item, ItemTo itemTo) {
        item.setPrice(itemTo.getPrice());
        return item;
    }

    public static List<ItemTo> getTos(Collection<Item> items) {
        return items.stream()
                .map(item -> new ItemTo(item.getId(), item.getDish().getId(), item.getDish().getName(), item.getPrice()))
                .collect(Collectors.toList());
    }

    public static ItemTo createTo(Item item) {
        return new ItemTo(item.getId(), item.getDish().getId(), item.getDish().getName(), item.getPrice());
    }
}
