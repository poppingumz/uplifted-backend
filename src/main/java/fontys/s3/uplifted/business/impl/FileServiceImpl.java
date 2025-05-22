package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.business.impl.mapper.FileMapper;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void uploadFile(MultipartFile file) {
        try {
            FileEntity entity = FileEntity.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .build();

            fileRepository.save(entity);
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new RuntimeException("Failed to upload file");
        }
    }

    @Override
    public List<File> getAllFiles() {
        return fileRepository.findAll()
                .stream()
                .map(FileMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id)
                .map(FileMapper::toDomain);
    }

    @Override
    public void deleteFile(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new RuntimeException("File not found with ID: " + id);
        }
        fileRepository.deleteById(id);
    }

    @Override
    public List<File> getFilesByCourse(Long courseId) {
        return fileRepository.findByCourseId(courseId)
                .stream()
                .map(FileMapper::toDomain)
                .collect(Collectors.toList());
    }


}
