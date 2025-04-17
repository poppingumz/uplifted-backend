package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.FileServiceImpl;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

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
    void uploadFile_success() {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "hello".getBytes());
        fileService.uploadFile(file);
        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    void getFileById_notFound() {
        when(fileRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> fileService.getFileById(99L));
    }
}
