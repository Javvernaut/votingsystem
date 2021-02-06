package javvernaut.votingsystem.web;

import javvernaut.votingsystem.util.exception.AppException;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";
    public static final String EXCEPTION_RESTAURANT_DUPLICATE_NAME = "Restaurant with this name already exists";
    public static final String EXCEPTION_DUPLICATE_DISH_NAME = "Dish with this name already exists in this restaurant";
    public static final String EXCEPTION_DUPLICATE_MENU_DATE = "Menu with this date already exists in this restaurant";
    public static final String EXCEPTION_DUPLICATE_ITEM = "item already exists in this menu";

    public static final Map<String, String> constraints_map = Map.of(
            "dishes_unique_name_restaurant_idx", EXCEPTION_DUPLICATE_DISH_NAME,
            "menus_unique_date_restaurant_idx", EXCEPTION_DUPLICATE_MENU_DATE,
            "items_unique_menu_dish_idx", EXCEPTION_DUPLICATE_ITEM
    );

    private final ErrorAttributes errorAttributes;

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appException(WebRequest request, AppException ex) {
        return createResponseEntity(getDefaultBody(request, ex.getOptions(), null), ex.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dbException(WebRequest request, DataIntegrityViolationException ex) {
        String rootMessage = getRootCause(ex).getMessage();
        if (rootMessage != null) {
            for (Map.Entry<String, String> constraintPair : constraints_map.entrySet()) {
                if (rootMessage.toLowerCase().contains(constraintPair.getKey())) {
                    return createResponseEntity(
                            getDefaultBody(
                                    request,
                                    ErrorAttributeOptions.defaults(),
                                    constraintPair.getValue()),
                            HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.defaults(), null), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindingErrors(ex.getBindingResult(), request);
    }

    private ResponseEntity<Object> handleBindingErrors(BindingResult result, WebRequest request) {
        String msg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        return createResponseEntity(getDefaultBody(request, ErrorAttributeOptions.defaults(), msg), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> getDefaultBody(WebRequest request, ErrorAttributeOptions options, String msg) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, options);
        if (msg != null) {
            body.put("message", msg);
        }
        return body;
    }

    private <T> ResponseEntity<T> createResponseEntity(Map<String, Object> body, HttpStatus status) {
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return (ResponseEntity<T>) ResponseEntity.status(status).body(body);
    }
}
