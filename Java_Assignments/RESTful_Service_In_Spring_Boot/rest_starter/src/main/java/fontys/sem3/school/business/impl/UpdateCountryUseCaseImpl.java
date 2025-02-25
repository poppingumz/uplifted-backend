package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.UpdateCountryUseCase;
import fontys.sem3.school.domain.UpdateCountryRequest;
import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateCountryUseCaseImpl implements UpdateCountryUseCase {
    private final CountryRepository countryRepository;

    @Override
    public void updateCountry(Long id, UpdateCountryRequest request) {
        CountryEntity existingCountry = countryRepository.findById(id);
        if (existingCountry == null) {
            throw new IllegalArgumentException("Country not found with ID: " + id);
        }

        existingCountry.setId(id);
        existingCountry.setName(request.getName());

        countryRepository.save(existingCountry);
    }
}
