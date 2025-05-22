package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.FileServiceImpl;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    private FileRepository fileRepository;
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        fileRepository = mock(FileRepository.class);
        fileService = new FileServiceImpl(fileRepository);
    }

    @Test
    void getFileById_found() {
        FileEntity fileEntity = FileEntity.builder()
                .id(1L).name("file.pdf").data(new byte[]{1, 2}).build();

        when(fileRepository.findById(1L)).thenReturn(Optional.of(fileEntity));

        Optional<File> result = fileService.getFileById(1L);

        assertTrue(result.isPresent());
        assertEquals("file.pdf", result.get().getName());
    }

    @Test
    void getFileById_notFound() {
        when(fileRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<File> result = fileService.getFileById(99L);

        assertTrue(result.isEmpty());
    }


    @Test
    void uploadFile_success() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes());

        fileService.uploadFile(mockFile);

        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    void deleteFile_success() {
        when(fileRepository.existsById(1L)).thenReturn(true);
        doNothing().when(fileRepository).deleteById(1L);

        fileService.deleteFile(1L);

        verify(fileRepository).deleteById(1L);
    }

    @Test
    void deleteFile_notFound() {
        when(fileRepository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> fileService.deleteFile(99L));
        assertEquals("File not found with ID: 99", ex.getMessage());
    }


    @Test
    void getFilesByCourse_success() {
        FileEntity file1 = FileEntity.builder()
                .id(1L).name("a.txt").courseId(5L).build();
        FileEntity file2 = FileEntity.builder()
                .id(2L).name("b.pdf").courseId(5L).build();

        when(fileRepository.findByCourseId(5L)).thenReturn(List.of(file1, file2));

        List<File> files = fileService.getFilesByCourse(5L);

        assertEquals(2, files.size());
        assertEquals("a.txt", files.get(0).getName());
        assertEquals("b.pdf", files.get(1).getName());
    }
}
