package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.ProgressService;
import fontys.s3.uplifted.domain.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressControllerTest {

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private ProgressController controller;

    private Progress sampleProgress;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProgress = new Progress(100L, 2L, 1L, 50.0);
    }

    @Test
    void shouldCreateProgressSuccess() {
        when(progressService.createProgress(any(Progress.class)))
                .thenReturn(sampleProgress);

        ResponseEntity<Progress> resp = controller.createProgress(sampleProgress);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(100L, resp.getBody().getId());
        assertEquals(50.0, resp.getBody().getProgressPercentage());
        verify(progressService).createProgress(sampleProgress);
    }

    @Test
    void shouldReturn500WhenCreateProgressThrows() {
        when(progressService.createProgress(any(Progress.class)))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<Progress> resp = controller.createProgress(sampleProgress);

        assertEquals(500, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(progressService).createProgress(sampleProgress);
    }

    @Test
    void shouldGetAllProgress() {
        Progress other = new Progress(101L, 3L, 2L, 80.0);
        when(progressService.getAllProgress())
                .thenReturn(List.of(sampleProgress, other));

        ResponseEntity<List<Progress>> resp = controller.getAllProgress();

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(2, resp.getBody().size());
        verify(progressService).getAllProgress();
    }

    @Test
    void shouldGetProgressByIdWhenFound() {
        when(progressService.getProgressById(100L))
                .thenReturn(Optional.of(sampleProgress));

        ResponseEntity<Progress> resp = controller.getProgressById(100L);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1L, resp.getBody().getUserId());
        assertEquals(2L, resp.getBody().getCourseId());
        verify(progressService).getProgressById(100L);
    }

    @Test
    void shouldReturn404WhenProgressNotFoundById() {
        when(progressService.getProgressById(200L))
                .thenReturn(Optional.empty());

        ResponseEntity<Progress> resp = controller.getProgressById(200L);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(progressService).getProgressById(200L);
    }

    @Test
    void shouldDeleteProgressSuccessfully() {
        when(progressService.deleteProgress(100L)).thenReturn(true);

        ResponseEntity<Void> resp = controller.deleteProgress(100L);

        assertEquals(204, resp.getStatusCodeValue());
        verify(progressService).deleteProgress(100L);
    }

    @Test
    void shouldReturn404WhenDeleteProgressNotFound() {
        when(progressService.deleteProgress(123L)).thenReturn(false);

        ResponseEntity<Void> resp = controller.deleteProgress(123L);

        assertEquals(404, resp.getStatusCodeValue());
        verify(progressService).deleteProgress(123L);
    }
}
