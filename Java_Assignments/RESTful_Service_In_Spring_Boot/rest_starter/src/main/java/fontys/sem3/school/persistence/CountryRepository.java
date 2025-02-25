package fontys.sem3.school.persistence;

import fontys.sem3.school.persistence.entity.CountryEntity;

import java.util.List;

public interface CountryRepository {
    boolean existsByCode(String code);

    boolean existsById(long countryId);

    CountryEntity findById(long countryId);

    CountryEntity save(CountryEntity country);

    List<CountryEntity> findAll();

    int count();

    void deleteCountry(Long id);
}
