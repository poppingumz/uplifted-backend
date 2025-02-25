package fontys.sem3.school.business;

import fontys.sem3.school.domain.CreateCountryRequest;
import fontys.sem3.school.domain.CreateCountryResponse;

public interface CreateCountryUseCase {
    CreateCountryResponse createCountry(CreateCountryRequest request);
}
