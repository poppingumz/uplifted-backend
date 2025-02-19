import java.util.Objects;

import lombok.*;

@Data
public class SignUpRequest {
    private String email;
    @ToString.Exclude
    private String password;
    private Address address;

    @Override
    public String toString() {
        return String.format("SignUpRequest(email=%s, address=Address(street=%s, number=%s, complement=%s, postalCode=%s, city=%s, state=%s))",
                email, address.getStreet(), address.getNumber(), address.getComplement(), address.getPostalCode(),
                address.getCity(), address.getState());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpRequest that = (SignUpRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, address);
    }
}
