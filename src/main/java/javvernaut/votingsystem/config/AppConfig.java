package javvernaut.votingsystem.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import javvernaut.votingsystem.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class AppConfig {
    public static final LocalDate CURRENT_DATE = LocalDate.of(2020, 12, 10);
    public static final LocalTime CURRENT_TIME = LocalTime.of(11, 0, 0);
    public static final LocalTime VOTES_DEADLINE = LocalTime.of(11, 0, 0);

/*    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }*/

    @Bean
    public Module hibernate5Module() {
        return new Hibernate5Module();
    }

    @Autowired
    public void storeObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }
}
