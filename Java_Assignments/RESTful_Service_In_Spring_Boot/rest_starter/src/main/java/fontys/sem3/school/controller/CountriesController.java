package fontys.sem3.school.controller;

import fontys.sem3.school.business.*;
import fontys.sem3.school.domain.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/countries")
@AllArgsConstructor
public class CountriesController {
    private final CreateCountryUseCase createCountryUseCase;
    private final GetCountriesUseCase getCountriesUseCase;
    private final GetCountryUseCase getCountryUseCase;
    private final UpdateCountryUseCase updateCountryUseCase;
    private final DeleteCountryUseCase deleteCountryUseCase;

    @GetMapping
    public ResponseEntity<GetCountriesResponse> getCountries() {
        return ResponseEntity.ok(getCountriesUseCase.getCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable("id") Long id) {
        Optional<Country> country = getCountryUseCase.getCountry(id);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CreateCountryResponse> createCountry(@RequestBody @Valid CreateCountryRequest request) {
        CreateCountryResponse response = createCountryUseCase.createCountry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCountry(@PathVariable("id") Long id, @RequestBody @Valid UpdateCountryRequest request) {
        updateCountryUseCase.updateCountry(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable("id") Long id) {
        deleteCountryUseCase.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
