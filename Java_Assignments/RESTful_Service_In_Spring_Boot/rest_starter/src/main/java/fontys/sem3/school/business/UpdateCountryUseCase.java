package fontys.sem3.school.business;

import fontys.sem3.school.domain.UpdateCountryRequest;

public interface UpdateCountryUseCase {
    void updateCountry(Long id, UpdateCountryRequest request);
}
