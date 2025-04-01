package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.domain.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            log.error("IOException while uploading file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file due to an I/O error.");
        } catch (Exception e) {
            log.error("Unexpected error while uploading file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while uploading the file.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<File>> getAllFiles() {
        try {
            return ResponseEntity.ok(fileService.getAllFiles());
        } catch (Exception e) {
            log.error("Failed to fetch files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        try {
            File file = fileService.getFileById(id);
            if (file != null) {
                return ResponseEntity.ok(file);
            } else {
                log.warn("File not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Failed to fetch file with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
