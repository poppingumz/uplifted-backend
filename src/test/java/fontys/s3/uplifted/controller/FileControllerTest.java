package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.domain.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
@Import(fontys.s3.uplifted.config.TestSecurityConfig.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setup() {
        mockFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());
    }

    @Test
    void testUploadFileSuccess() throws Exception {
        mockMvc.perform(multipart("/files/upload")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully."));
    }

    @Test
    void testUploadFileIOException() throws Exception {
        doThrow(new IOException("Disk error")).when(fileService).uploadFile(any());

        mockMvc.perform(multipart("/files/upload")
                        .file(mockFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to upload file due to an I/O error."));
    }

    @Test
    void testUploadFileUnexpectedException() throws Exception {
        doThrow(new RuntimeException("Oops")).when(fileService).uploadFile(any());

        mockMvc.perform(multipart("/files/upload")
                        .file(mockFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while uploading the file."));
    }

    @Test
    void testGetAllFilesSuccess() throws Exception {
        File f1 = new File(1L, "file1.pdf", "pdf", "dummy content 1".getBytes(), 1L, 1L);
        File f2 = new File(2L, "file2.docx", "docx", "dummy content 2".getBytes(), 2L, 2L);

        when(fileService.getAllFiles()).thenReturn(List.of(f1, f2));

        mockMvc.perform(get("/files/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetFileByIdFound() throws Exception {
        File file = new File(10L, "image.jpg", "jpg", "image bytes".getBytes(), 1L, 1L);

        when(fileService.getFileById(10L)).thenReturn(file);

        mockMvc.perform(get("/files/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("image.jpg"));
    }

    @Test
    void testGetFileByIdNotFound() throws Exception {
        when(fileService.getFileById(123L)).thenReturn(null);

        mockMvc.perform(get("/files/123"))
                .andExpect(status().isNotFound());
    }
}
