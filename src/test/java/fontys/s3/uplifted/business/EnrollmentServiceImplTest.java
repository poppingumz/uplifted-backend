package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.business.impl.EnrollmentServiceImpl;
import fontys.s3.uplifted.domain.Enrollment;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Enrollment enrollment;
    private EnrollmentEntity enrollmentEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        enrollment = new Enrollment(1L, 1L, 1L, LocalDate.now());
        enrollmentEntity = new EnrollmentEntity(1L, 1L, 1L, LocalDate.now());
    }

    @Test
    void shouldEnrollUserSuccessfully() {
        when(enrollmentRepository.isUserEnrolledInCourse(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.createEnrollment(any(EnrollmentEntity.class))).thenReturn(enrollmentEntity);

        assertDoesNotThrow(() -> enrollmentService.enrollUser(1L, 1L));
        verify(enrollmentRepository, times(1)).createEnrollment(any(EnrollmentEntity.class));
    }

    @Test
    void shouldNotAllowDuplicateEnrollment() {
        when(enrollmentRepository.isUserEnrolledInCourse(1L, 1L)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> enrollmentService.enrollUser(1L, 1L));
        assertEquals("User is already enrolled in this course.", exception.getMessage());
        verify(enrollmentRepository, never()).createEnrollment(any(EnrollmentEntity.class));
    }
}
