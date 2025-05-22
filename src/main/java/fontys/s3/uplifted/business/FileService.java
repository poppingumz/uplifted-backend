package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {
    void uploadFile(MultipartFile file) throws IOException;
    List<File> getAllFiles();
    Optional<File> getFileById(Long id);
    void deleteFile(Long id);
    List<File> getFilesByCourse(Long courseId);
}
