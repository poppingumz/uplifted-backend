package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.business.FileService;
import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.entity.FileEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploaderId") Long uploaderId,
            @RequestParam("courseId") Long courseId
    ) {
        try {
            Long fileId = fileService.uploadFile(file, uploaderId, courseId);
            return ResponseEntity.ok(fileId);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<File>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        return fileService.getFileById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id,
            Authentication authentication
    ) {
        FileEntity file = fileService.getRawFileEntityById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "File not found"));

        Long courseId = file.getCourse().getId();

        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "User not recognized"));

        boolean isEnrolled = enrollmentService.getEnrolledStudents(courseId)
                .contains(userId);
        if (!isEnrolled) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You must be enrolled to download this file");
        }

        ByteArrayResource resource = new ByteArrayResource(file.getData());
        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(file.getName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .contentLength(file.getData().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }


}
