import java.util.Objects;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {
    private String street;
    private String number;
    private String complement;
    private String postalCode;
    private String city;
    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(number, address.number) && Objects.equals(complement, address.complement) && Objects.equals(postalCode, address.postalCode) && Objects.equals(city, address.city) && Objects.equals(state, address.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, complement, postalCode, city, state);
    }
}
