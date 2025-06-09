package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {
    Long uploadFile(MultipartFile file, Long uploaderId, Long courseId) throws IOException;

    List<File> getAllFiles();

    Optional<File> getFileById(Long id);

    Optional<FileEntity> getRawFileEntityById(Long id);

    void deleteFile(Long id);

    List<File> getFilesByCourse(Long courseId);
}
