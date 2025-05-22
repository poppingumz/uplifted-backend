package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.domain.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );
    }

    @Test
    void shouldUploadFileSuccess() throws Exception {
        // service does nothing => success
        doNothing().when(fileService).uploadFile(mockFile);

        ResponseEntity<String> response = fileController.uploadFile(mockFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());
        verify(fileService).uploadFile(mockFile);
    }

    @Test
    void shouldUploadFileIOException() throws Exception {
        doThrow(new IOException("Disk error"))
                .when(fileService).uploadFile(mockFile);

        ResponseEntity<String> response = fileController.uploadFile(mockFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to upload file due to an I/O error.", response.getBody());
        verify(fileService).uploadFile(mockFile);
    }

    @Test
    void shouldUploadFileUnexpectedError() throws Exception {
        doThrow(new RuntimeException("Oops"))
                .when(fileService).uploadFile(mockFile);

        ResponseEntity<String> response = fileController.uploadFile(mockFile);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred while uploading the file.", response.getBody());
        verify(fileService).uploadFile(mockFile);
    }

    @Test
    void shouldReturnAllFiles() {
        File f1 = new File(1L, "file1.pdf", "pdf", new byte[]{1}, 1L, 1L);
        File f2 = new File(2L, "file2.docx", "docx", new byte[]{2}, 2L, 2L);
        when(fileService.getAllFiles()).thenReturn(List.of(f1, f2));

        ResponseEntity<List<File>> response = fileController.getAllFiles();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(fileService).getAllFiles();
    }

    @Test
    void shouldReturnFileByIdWhenFound() {
        File file = new File(10L, "img.jpg", "jpg", new byte[]{3}, 1L, 1L);
        when(fileService.getFileById(10L)).thenReturn(Optional.of(file));

        ResponseEntity<File> response = fileController.getFileById(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10L, response.getBody().getId());
        verify(fileService).getFileById(10L);
    }

    @Test
    void shouldReturn404WhenFileNotFound() {
        when(fileService.getFileById(123L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> fileController.getFileById(123L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("File not found", ex.getReason());
        verify(fileService).getFileById(123L);
    }
}
