// ProgressServiceImplTest.java
package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.ProgressServiceImpl;
import fontys.s3.uplifted.persistence.ProgressRepository;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import fontys.s3.uplifted.domain.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressServiceImplTest {

    @Mock ProgressRepository progressRepository;
    @Mock CourseRepository courseRepository;
    @Mock UserRepository userRepository;
    @InjectMocks
    ProgressServiceImpl service;

    private ProgressEntity entity;
    private CourseEntity courseEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseEntity = new CourseEntity(); courseEntity.setId(2L);
        userEntity = new UserEntity(); userEntity.setId(3L);
        entity = new ProgressEntity();
        entity.setId(10L);
        entity.setCourse(courseEntity);
        entity.setUser(userEntity);
        entity.setProgressPercentage(75.0);
    }

    @Test
    void getAllProgress() {
        when(progressRepository.findAll()).thenReturn(List.of(entity));
        var result = service.getAllProgress();
        assertEquals(1, result.size());
        assertEquals(75.0, result.get(0).getProgressPercentage());
    }

    @Test
    void getByIdFound() {
        when(progressRepository.findById(10L)).thenReturn(Optional.of(entity));
        var opt = service.getProgressById(10L);
        assertTrue(opt.isPresent());
        assertEquals(75.0, opt.get().getProgressPercentage());
    }

    @Test
    void getByIdNotFound() {
        when(progressRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(service.getProgressById(99L).isEmpty());
    }

    @Test
    void createProgressSuccess() {
        when(courseRepository.findById(2L)).thenReturn(Optional.of(courseEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));
        when(progressRepository.save(any())).thenReturn(entity);
        Progress created = service.createProgress(new Progress(null, 2L, 3L, 75.0));
        assertEquals(10L, created.getId());
    }

    @Test
    void createProgressMissingCourse() {
        when(courseRepository.findById(any())).thenReturn(Optional.empty());
        var ex = assertThrows(RuntimeException.class,
                () -> service.createProgress(new Progress(null, 2L, 3L, 75.0)));
        assertEquals("Course not found", ex.getMessage());
    }

    @Test
    void updateProgressSuccess() {
        when(courseRepository.findById(2L)).thenReturn(Optional.of(courseEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));
        when(progressRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(progressRepository.save(any())).thenReturn(entity);
        var updated = service.updateProgress(10L, new Progress(null, 2L, 3L, 75.0));
        assertTrue(updated.isPresent());
        assertEquals(75.0, updated.get().getProgressPercentage());
    }

    @Test
    void updateProgressNotFoundId() {
        when(courseRepository.findById(2L)).thenReturn(Optional.of(courseEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));
        when(progressRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(service.updateProgress(99L, new Progress(null, 2L, 3L, 75.0)).isEmpty());
    }

    @Test
    void deleteProgress() {
        when(progressRepository.existsById(10L)).thenReturn(true);
        assertTrue(service.deleteProgress(10L));
        verify(progressRepository).deleteById(10L);
    }

    @Test
    void deleteProgressNotFound() {
        when(progressRepository.existsById(99L)).thenReturn(false);
        assertFalse(service.deleteProgress(99L));
    }
}
