package fontys.sem3.school.business;

import fontys.sem3.school.business.exception.CountryCodeAlreadyExistsException;
import fontys.sem3.school.persistence.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CountryCodeValidator {
    private final CountryRepository countryRepository;

    public void validateCode(String code) {
        if (countryRepository.existsByCode(code)) {
            throw new CountryCodeAlreadyExistsException("Country code must be unique: " + code);
        }
    }
}
