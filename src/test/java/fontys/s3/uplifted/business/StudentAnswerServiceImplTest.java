package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.StudentAnswerServiceImpl;
import fontys.s3.uplifted.persistence.entity.StudentAnswerEntity;
import fontys.s3.uplifted.domain.StudentAnswer;
import fontys.s3.uplifted.persistence.StudentAnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentAnswerServiceImplTest {

    @Mock
    private StudentAnswerRepository repo;

    @InjectMocks
    private StudentAnswerServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitAnswer() {
        StudentAnswer ans = new StudentAnswer(null, 1L, 2L, 3L, "ans", null, null, false);
        service.submitAnswer(ans);
        ArgumentCaptor<StudentAnswerEntity> captor = ArgumentCaptor.forClass(StudentAnswerEntity.class);
        verify(repo).save(captor.capture());
        assertFalse(captor.getValue().isReviewed());
    }

    @Test
    void reviewAnswerSuccess() {
        StudentAnswerEntity ent = new StudentAnswerEntity();
        ent.setId(5L);
        when(repo.findById(5L)).thenReturn(Optional.of(ent));

        service.reviewAnswer(5L, 90, "good");

        assertTrue(ent.isReviewed());
        assertEquals(90, ent.getAwardedMarks());
        assertEquals("good", ent.getMentorFeedback());
        verify(repo).save(ent);
    }

    @Test
    void reviewAnswerNotFound() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.reviewAnswer(99L, 90, "good"));
    }

    @Test
    void getPendingReviews() {
        StudentAnswerEntity e = new StudentAnswerEntity();
        e.setReviewed(false);
        when(repo.findByReviewedFalse()).thenReturn(List.of(e));

        var list = service.getPendingReviews();
        assertEquals(1, list.size());
    }

    @Test
    void getAnswersByUser() {
        StudentAnswerEntity e = new StudentAnswerEntity();
        e.setUserId(2L);
        when(repo.findByUserId(2L)).thenReturn(List.of(e));

        var list = service.getAnswersByUser(2L);
        assertEquals(1, list.size());
    }
}
