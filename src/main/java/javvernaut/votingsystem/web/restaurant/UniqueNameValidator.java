package javvernaut.votingsystem.web.restaurant;

import javvernaut.votingsystem.HasIdAndName;
import javvernaut.votingsystem.repository.RestaurantRepository;
import javvernaut.votingsystem.web.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UniqueNameValidator implements Validator {

    private final RestaurantRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndName.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndName hasNameObject = (HasIdAndName) target;
        if (StringUtils.hasText(hasNameObject.getName())) {
            if (repository.findByNameIgnoreCase(hasNameObject.getName())
                    .filter(restaurant -> !restaurant.getId().equals(hasNameObject.getId())).isPresent()) {
                errors.rejectValue("name", "", GlobalExceptionHandler.EXCEPTION_RESTAURANT_DUPLICATE_NAME);
            }
        }
    }
}
