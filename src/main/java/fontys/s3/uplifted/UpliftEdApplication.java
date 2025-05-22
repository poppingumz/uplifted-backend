package fontys.s3.uplifted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.flywaydb.core.Flyway;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UpliftEdApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UpliftEdApplication.class, args);
        Flyway flyway = context.getBean(Flyway.class);
        flyway.repair();
    }
}

