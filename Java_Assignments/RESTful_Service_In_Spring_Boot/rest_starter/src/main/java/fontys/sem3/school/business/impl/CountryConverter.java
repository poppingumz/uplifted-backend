package fontys.sem3.school.business.impl;

import fontys.sem3.school.domain.Country;
import fontys.sem3.school.persistence.entity.CountryEntity;

final class CountryConverter {
    private CountryConverter() {
    }

    public static Country convert(CountryEntity country) {
        return Country.builder()
                .id(country.getId())
                .code(country.getCode())
                .name(country.getName())
                .build();
    }
}
