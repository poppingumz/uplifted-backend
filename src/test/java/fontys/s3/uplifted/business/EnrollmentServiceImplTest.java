package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceImplTest {

    private EnrollmentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EnrollmentServiceImpl();
    }

    @Test
    void enrollAndRetrieve() {
        service.enrollStudent(1L, 100L);
        Set<Long> students = service.getEnrolledStudents(1L);
        assertTrue(students.contains(100L));
    }

    @Test
    void unenrollExisting() {
        service.enrollStudent(1L, 100L);
        service.unenrollStudent(1L, 100L);
        assertFalse(service.getEnrolledStudents(1L).contains(100L));
    }

    @Test
    void unenrollNotPresent() {
        service.enrollStudent(1L, 100L);
        service.unenrollStudent(1L, 200L);
        assertEquals(Set.of(100L), service.getEnrolledStudents(1L));
    }
}
