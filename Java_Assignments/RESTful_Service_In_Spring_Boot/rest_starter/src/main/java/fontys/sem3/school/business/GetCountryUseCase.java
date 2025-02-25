package fontys.sem3.school.business;

import fontys.sem3.school.domain.Country;
import java.util.Optional;

public interface GetCountryUseCase {
    Optional<Country> getCountry(Long id);
}
