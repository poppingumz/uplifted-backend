package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.GetCountryUseCase;
import fontys.sem3.school.domain.Country;
import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetCountryUseCaseImpl implements GetCountryUseCase {
    private final CountryRepository countryRepository;

    @Override
    public Optional<Country> getCountry(Long id) {
        CountryEntity countryEntity = countryRepository.findById(id);
        return Optional.ofNullable(countryEntity != null ? mapToDomain(countryEntity) : null);
    }

    private Country mapToDomain(CountryEntity entity) {
        return Country.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }
}
