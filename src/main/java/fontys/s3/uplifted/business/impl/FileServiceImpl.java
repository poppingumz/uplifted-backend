package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void uploadFile(MultipartFile file) {
        try {
            File uploadedFile = File.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .data(file.getBytes())
                    .build();

            fileRepository.saveFile(uploadedFile);
            log.info("Successfully uploaded file: {}", uploadedFile.getName());
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Could not upload file. Please try again.");
        }
    }

    public List<File> getAllFiles() {
        try {
            return fileRepository.getAllFiles();
        } catch (Exception e) {
            log.error("Error retrieving all files", e);
            throw new RuntimeException("Could not retrieve files.");
        }
    }

    public File getFileById(Long id) {
        try {
            return fileRepository.getFileById(id);
        } catch (Exception e) {
            log.error("Error retrieving file by ID {}", id, e);
            throw new RuntimeException("File not found with ID: " + id);
        }
    }
}
