package fontys.sem3.school.business;

import fontys.sem3.school.business.exception.InvalidCountryException;

public interface CountryIdValidator {
    void validateId(Long countryId) throws InvalidCountryException;
}
