package fontys.sem3.school.configuration.db;

import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseDataInitializer {

    private CountryRepository countryRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {
        if (countryRepository.count() == 0) {
            countryRepository.save(CountryEntity.builder().code("NL").name("Netherlands").build());
            countryRepository.save(CountryEntity.builder().code("BG").name("Bulgaria").build());
            countryRepository.save(CountryEntity.builder().code("RO").name("Romania").build());
            countryRepository.save(CountryEntity.builder().code("BR").name("Brazil").build());
            countryRepository.save(CountryEntity.builder().code("CN").name("China").build());
        }
    }
}
