package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void uploadFile(MultipartFile file) throws IOException;
    List<File> getAllFiles();
    File getFileById(Long id);
}
