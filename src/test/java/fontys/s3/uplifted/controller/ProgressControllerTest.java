package fontys.s3.uplifted.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.business.ProgressService;
import fontys.s3.uplifted.domain.Progress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressController.class)
@Import(fontys.s3.uplifted.config.TestSecurityConfig.class)
public class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    @Autowired
    private ObjectMapper objectMapper;

    private Progress sampleProgress;

    @BeforeEach
    void setup() {
        sampleProgress = new Progress(100L, 2L, 1L, 50.0); // id, courseId, userId, progress%
    }

    @Test
    void testCreateProgressSuccess() throws Exception {
        when(progressService.createProgress(any())).thenReturn(sampleProgress);

        mockMvc.perform(post("/api/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProgress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.progressPercentage").value(50.0));
    }

    @Test
    void testGetAllProgressSuccess() throws Exception {
        Progress p2 = new Progress(101L, 4L, 3L, 80.0);
        when(progressService.getAllProgress()).thenReturn(List.of(sampleProgress, p2));

        mockMvc.perform(get("/api/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetProgressByIdFound() throws Exception {
        when(progressService.getProgressById(100L)).thenReturn(Optional.of(sampleProgress));

        mockMvc.perform(get("/api/progress/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.courseId").value(2));
    }

    @Test
    void testGetProgressByIdNotFound() throws Exception {
        when(progressService.getProgressById(200L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/progress/200"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProgressSuccess() throws Exception {
        when(progressService.deleteProgress(100L)).thenReturn(true);

        mockMvc.perform(delete("/api/progress/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProgressNotFound() throws Exception {
        when(progressService.deleteProgress(123L)).thenReturn(false);

        mockMvc.perform(delete("/api/progress/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProgressThrowsException() throws Exception {
        when(progressService.createProgress(any())).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProgress)))
                .andExpect(status().isInternalServerError());
    }
}
