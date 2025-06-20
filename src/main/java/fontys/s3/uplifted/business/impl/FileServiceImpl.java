package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.business.impl.exception.FileNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.FileMapper;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public Long uploadFile(MultipartFile file, Long uploaderId, Long courseId) throws IOException {
        UserEntity uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new RuntimeException("Uploader not found"));
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        FileEntity entity = FileEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .uploader(uploader)
                .course(course)
                .uploadDate(LocalDate.now())
                .build();

        return fileRepository.save(entity).getId();
    }

    @Override
    public List<File> getAllFiles() {
        return fileRepository.findAll()
                .stream()
                .map(FileMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id)
                .map(FileMapper::toDomain);
    }

    @Override
    public Optional<FileEntity> getRawFileEntityById(Long id) {
        return fileRepository.findById(id);
    }

    @Override
    public void deleteFile(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new FileNotFoundException("File not found with ID: " + id);
        }
        fileRepository.deleteById(id);
    }

    @Override
    public List<File> getFilesByCourse(Long courseId) {
        return fileRepository.findByCourseId(courseId)
                .stream()
                .map(FileMapper::toDomain)
                .toList();
    }
}
