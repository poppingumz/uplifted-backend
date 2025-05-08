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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file due to an I/O error.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while uploading the file.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<File>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        File file = fileService.getFileById(id);
        return file != null ? ResponseEntity.ok(file) : ResponseEntity.notFound().build();
    }
}
