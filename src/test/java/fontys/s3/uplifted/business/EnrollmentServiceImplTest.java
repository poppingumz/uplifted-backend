package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.EnrollmentServiceImpl;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {

    private EnrollmentRepository enrollmentRepository;
    private EnrollmentServiceImpl service;

    @BeforeEach
    void setUp() {
        enrollmentRepository = mock(EnrollmentRepository.class);
        service = new EnrollmentServiceImpl(enrollmentRepository);
    }

    @Test
    void enrollAndRetrieve() {
        // mock behavior
        when(enrollmentRepository.findByCourseId(1L))
                .thenReturn(List.of(new EnrollmentEntity(1L, 100L)));

        service.enrollStudent(1L, 100L);
        Set<Long> students = service.getEnrolledStudents(1L);

        assertTrue(students.contains(100L));
        verify(enrollmentRepository).save(any(EnrollmentEntity.class));
    }

    @Test
    void unenrollExisting() {
        service.unenrollStudent(1L, 100L);
        verify(enrollmentRepository).deleteById(any());
    }

    @Test
    void unenrollNotPresent() {
        when(enrollmentRepository.findByCourseId(1L))
                .thenReturn(List.of(new EnrollmentEntity(1L, 100L)));

        service.unenrollStudent(1L, 200L);

        Set<Long> students = service.getEnrolledStudents(1L);
        assertEquals(Set.of(100L), students);
    }
}
