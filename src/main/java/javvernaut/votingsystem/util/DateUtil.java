package javvernaut.votingsystem.util;

import javvernaut.votingsystem.util.exception.IllegalRequestDataException;

import java.time.LocalDate;

import static javvernaut.votingsystem.config.AppConfig.CURRENT_DATE;

public class DateUtil {
    public static void checkDateIsAfterTheCurrent(LocalDate date, String message) {
        if (!date.isAfter(CURRENT_DATE)) {
            throw new IllegalRequestDataException(message);
        }
    }
}
