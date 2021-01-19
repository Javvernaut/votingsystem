package javvernaut.votingsystem.util;

import javvernaut.votingsystem.util.exception.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class DateUtil {
    public static LocalDate current_date = LocalDate.of(2020, 12, 9);
    public static LocalTime current_time = LocalTime.of(11, 0, 0);
    public static LocalTime votes_deadline = LocalTime.of(11, 0, 0);

    public static void checkDateIsAfterTheCurrent(LocalDate date, String message) {
        if (!date.isAfter(current_date)) {
            throw new IllegalRequestDataException(message);
        }
    }
}
