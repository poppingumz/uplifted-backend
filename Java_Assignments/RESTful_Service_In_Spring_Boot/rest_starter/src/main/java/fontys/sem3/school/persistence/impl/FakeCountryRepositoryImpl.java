package fontys.sem3.school.persistence.impl;

import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FakeCountryRepositoryImpl implements CountryRepository {
    private static long NEXT_ID = 1;

    private final List<CountryEntity> savedCountries;

    public FakeCountryRepositoryImpl() {
        this.savedCountries = new ArrayList<>();
    }

    @Override
    public boolean existsByCode(String code) {
        return this.savedCountries
                .stream()
                .anyMatch(countryEntity -> countryEntity.getCode().equals(code));
    }

    @Override
    public boolean existsById(long countryId) {
        return this.savedCountries
                .stream()
                .anyMatch(countryEntity -> countryEntity.getId() == countryId);
    }

    @Override
    public CountryEntity findById(long countryId) {
        return this.savedCountries
                .stream()
                .filter(countryEntity -> countryEntity.getId() == countryId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public CountryEntity save(CountryEntity country) {
        if (country.getId() == null) {
            country.setId(NEXT_ID);
            NEXT_ID++;
            this.savedCountries.add(country);
        } else {
            for (int i = 0; i < savedCountries.size(); i++) {
                if (savedCountries.get(i).getId().equals(country.getId())) {
                    savedCountries.set(i, country);
                    return country;
                }
            }
        }
        return country;
    }

    @Override
    public List<CountryEntity> findAll() {
        return Collections.unmodifiableList(savedCountries);
    }

    @Override
    public int count() {
        return this.savedCountries.size();
    }

    @Override
    public void deleteCountry(Long id) {
        this.savedCountries.removeIf(countryEntity -> countryEntity.getId().equals(id));
    }
}
