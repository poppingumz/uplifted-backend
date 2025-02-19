import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SignUpRequestTest {

    @Test
    void toString_shouldReturnAllFieldsExceptPassword() {
        SignUpRequest signUpRequest = createTestRequest();

        String actual = signUpRequest.toString();

        String expected = "SignUpRequest(email=123@gmail.com, address=Address(street=My street, number=19A, complement=null, postalCode=1234AB, city=Eindhoven, state=North Brabant))";
        assertEquals(expected, actual);
    }

    @Test
    void equals_shouldReturnTrue_whenFieldsHaveSameValue() {
        SignUpRequest requestOne = createTestRequest();
        SignUpRequest requestTwo = createTestRequest();
        assertEquals(requestOne, requestTwo);
    }

    @Test
    void equals_shouldReturnFalse_whenFieldsHaveDifferentValue() {
        SignUpRequest requestOne = createTestRequest();
        SignUpRequest requestTwo = createTestRequest();
        requestTwo.setEmail("otheruser@gmail.com");
        assertNotEquals(requestOne, requestTwo);
    }

    private SignUpRequest createTestRequest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("123@gmail.com");
        signUpRequest.setPassword("s3cr3t");

        Address address = new Address();
        address.setCity("Eindhoven");
        address.setStreet("My street");
        address.setNumber("19A");
        address.setPostalCode("1234AB");
        address.setState("North Brabant");
        signUpRequest.setAddress(address);
        return signUpRequest;
    }
}