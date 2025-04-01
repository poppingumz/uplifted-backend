package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.File;

import java.util.List;

public interface FileRepository {
    void saveFile(File file);
    List<File> getAllFiles();
    File getFileById(Long id);
}
