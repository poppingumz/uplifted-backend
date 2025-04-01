package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.FileRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeFileRepositoryImpl implements FileRepository {

    private final List<File> files = new ArrayList<>();
    private Long currentId = 1L;

    public void saveFile(File file) {
        file.setId(currentId++);
        files.add(file);
    }

    public List<File> getAllFiles() {
        return new ArrayList<>(files);
    }

    public File getFileById(Long id) {
        return files.stream().filter(file -> file.getId().equals(id)).findFirst().orElse(null);
    }
}
