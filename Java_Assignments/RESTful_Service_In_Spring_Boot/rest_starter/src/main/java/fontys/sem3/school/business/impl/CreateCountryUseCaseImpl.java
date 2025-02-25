package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.CreateCountryUseCase;
import fontys.sem3.school.domain.CreateCountryRequest;
import fontys.sem3.school.domain.CreateCountryResponse;
import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateCountryUseCaseImpl implements CreateCountryUseCase {
    private final CountryRepository countryRepository;

    @Override
    public CreateCountryResponse createCountry(CreateCountryRequest request) {
        if (countryRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Country code must be unique");
        }

        CountryEntity savedCountry = countryRepository.save(
                CountryEntity.builder()
                        .code(request.getCode())
                        .name(request.getName())
                        .build()
        );

        return CreateCountryResponse.builder().countryId(savedCountry.getId()).build();
    }
}
