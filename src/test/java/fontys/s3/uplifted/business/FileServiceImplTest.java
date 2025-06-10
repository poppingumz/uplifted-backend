package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.FileServiceImpl;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    private FileRepository fileRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        fileRepository = mock(FileRepository.class);
        userRepository = mock(UserRepository.class);
        courseRepository = mock(CourseRepository.class);
        fileService = new FileServiceImpl(fileRepository, userRepository, courseRepository);
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
    void uploadFile_success() throws Exception {
        Long uploaderId = 10L;
        Long courseId = 20L;

        UserEntity uploader = UserEntity.builder().id(uploaderId).username("user").build();
        CourseEntity course = CourseEntity.builder().id(courseId).title("Test").build();
        FileEntity savedFile = FileEntity.builder().id(999L).build();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes());

        when(userRepository.findById(uploaderId)).thenReturn(Optional.of(uploader));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(savedFile);

        Long result = fileService.uploadFile(mockFile, uploaderId, courseId);

        assertEquals(999L, result);
        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    void uploadFile_uploaderNotFound() {
        Long uploaderId = 99L;
        Long courseId = 20L;
        MockMultipartFile mockFile = new MockMultipartFile("file", "a.txt", "text/plain", "data".getBytes());

        when(userRepository.findById(uploaderId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                fileService.uploadFile(mockFile, uploaderId, courseId));

        assertEquals("Uploader not found", ex.getMessage());
    }

    @Test
    void uploadFile_courseNotFound() {
        Long uploaderId = 10L;
        Long courseId = 999L;
        UserEntity uploader = UserEntity.builder().id(uploaderId).username("user").build();
        MockMultipartFile mockFile = new MockMultipartFile("file", "a.txt", "text/plain", "data".getBytes());

        when(userRepository.findById(uploaderId)).thenReturn(Optional.of(uploader));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                fileService.uploadFile(mockFile, uploaderId, courseId));

        assertEquals("Course not found", ex.getMessage());
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
        CourseEntity course = CourseEntity.builder().id(5L).build();

        FileEntity file1 = FileEntity.builder().id(1L).name("a.txt").course(course).build();
        FileEntity file2 = FileEntity.builder().id(2L).name("b.pdf").course(course).build();

        when(fileRepository.findByCourseId(5L)).thenReturn(List.of(file1, file2));

        List<File> files = fileService.getFilesByCourse(5L);

        assertEquals(2, files.size());
        assertEquals("a.txt", files.get(0).getName());
        assertEquals("b.pdf", files.get(1).getName());
    }

    @Test
    void getAllFiles_success() {
        FileEntity file1 = FileEntity.builder().id(1L).name("x.doc").build();
        FileEntity file2 = FileEntity.builder().id(2L).name("y.xls").build();

        when(fileRepository.findAll()).thenReturn(List.of(file1, file2));

        List<File> result = fileService.getAllFiles();

        assertEquals(2, result.size());
    }

    @Test
    void getRawFileEntityById_success() {
        FileEntity file = FileEntity.builder().id(42L).name("raw.png").build();
        when(fileRepository.findById(42L)).thenReturn(Optional.of(file));

        Optional<FileEntity> result = fileService.getRawFileEntityById(42L);

        assertTrue(result.isPresent());
        assertEquals("raw.png", result.get().getName());
    }

    @Test
    void getRawFileEntityById_notFound() {
        when(fileRepository.findById(123L)).thenReturn(Optional.empty());

        Optional<FileEntity> result = fileService.getRawFileEntityById(123L);

        assertTrue(result.isEmpty());
    }
}
