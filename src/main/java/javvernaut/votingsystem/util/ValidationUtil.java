package javvernaut.votingsystem.util;

import javvernaut.votingsystem.HasId;
import javvernaut.votingsystem.util.exception.IllegalRequestDataException;
import javvernaut.votingsystem.util.exception.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.Optional;


@UtilityClass
public class ValidationUtil {
    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, int id) {
        return checkNotFoundWithId(optional, "Entity with id=" + id +" not found");
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new NotFoundException(message));
    }

    public static void checkSingleModification(int count, String msg) {
        if (count != 1) {
            throw new NotFoundException(msg);
        }
    }
}
