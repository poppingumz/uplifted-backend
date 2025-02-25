package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.GetCountriesUseCase;
import fontys.sem3.school.domain.Country;
import fontys.sem3.school.domain.GetCountriesResponse;
import fontys.sem3.school.persistence.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetCountriesUseCaseImpl implements GetCountriesUseCase {
    private final CountryRepository countryRepository;

    @Override
    public GetCountriesResponse getCountries() {
        List<Country> countries = countryRepository.findAll()
                .stream()
                .map(CountryConverter::convert)
                .toList();

        return GetCountriesResponse.builder()
                .countries(countries)
                .build();
    }
}
