package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.CountryIdValidator;
import fontys.sem3.school.business.exception.InvalidCountryException;
import fontys.sem3.school.persistence.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CountryIdValidatorImpl implements CountryIdValidator {
    private final CountryRepository countryRepository;

    @Override
    public void validateId(Long countryId) {
        if (countryId == null || !countryRepository.existsById(countryId)) {
            throw new InvalidCountryException();
        }
    }
}
