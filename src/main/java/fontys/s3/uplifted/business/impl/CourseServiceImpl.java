package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll().stream()
                .filter(CourseEntity::isPublished)
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toDomain);
    }

    @Override
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Course createCourse(Course course, List<MultipartFile> uploadedFiles) {
        UserEntity instructor = userRepository.findById(course.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        CourseEntity saved = courseRepository.save(CourseMapper.toEntity(course, instructor));
        attachFilesToContents(saved, uploadedFiles, instructor);
        CourseEntity result = courseRepository.save(saved);

        NotificationMessage message = new NotificationMessage(
                result.getId(),
                "A new course has been created: " + result.getTitle(),
                result.getCategory().name()
        );
        messagingTemplate.convertAndSend("/topic/course/" + result.getId(), message);
        messagingTemplate.convertAndSend("/topic/category/" + result.getCategory().name(), message);

        return CourseMapper.toDomain(result);
    }

    @Transactional
    @Override
    public Optional<Course> updateCourse(Long id, Course course, List<MultipartFile> uploadedFiles) {
        return courseRepository.findById(id).map(existing -> {
            UserEntity instructor = userRepository.findById(course.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            // Apply updates
            existing.setTitle(course.getTitle());
            existing.setDescription(course.getDescription());
            existing.setCategory(course.getCategory());
            existing.setEnrollmentLimit(course.getEnrollmentLimit());
            existing.setPublished(course.isPublished());
            existing.setInstructor(instructor);

            attachFilesToContents(existing, uploadedFiles, instructor);

            CourseEntity saved = courseRepository.save(existing);

            NotificationMessage message = new NotificationMessage(
                    saved.getId(),
                    "Course updated: " + saved.getTitle(),
                    saved.getCategory().name()
            );

            messagingTemplate.convertAndSend("/topic/course/" + saved.getId(), message);
            messagingTemplate.convertAndSend("/topic/category/" + saved.getCategory().name(), message);

            return CourseMapper.toDomain(saved);
        });
    }

    @Override
    public boolean deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course with ID " + id + " not found.");
        }
        courseRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Course> getCoursesByEnrolledUser(Long userId) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getEnrolledStudents().stream()
                        .anyMatch(student -> student.getId().equals(userId)))
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void enrollInCourse(Long courseId, String username) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (course.getEnrolledStudents().contains(user)) {
            throw new RuntimeException("User already enrolled in this course.");
        }
        if (course.getEnrollmentLimit() > 0 && course.getEnrolledStudents().size() >= course.getEnrollmentLimit()) {
            throw new RuntimeException("Course enrollment limit reached.");
        }
        course.getEnrolledStudents().add(user);
        courseRepository.save(course);
    }

    private void attachFilesToContents(CourseEntity courseEntity, List<MultipartFile> uploadedFiles, UserEntity instructor) {
        if (courseEntity.getParts() == null) {
            log.warn("Course has no parts, skipping file attachment.");
            return;
        }

        int idx = 0;
        for (CoursePartEntity part : courseEntity.getParts()) {
            if (part.getContents() == null) continue;

            for (CoursePartContentEntity content : part.getContents()) {
                if (content.getContentType() == ContentType.FILE) {
                    if (uploadedFiles != null && idx < uploadedFiles.size()) {
                        MultipartFile mf = uploadedFiles.get(idx++);
                        try {
                            FileEntity f = fileRepository.save(FileEntity.builder()
                                    .name(mf.getOriginalFilename())
                                    .type(mf.getContentType())
                                    .course(courseEntity)
                                    .uploader(instructor)
                                    .data(mf.getBytes())
                                    .uploadDate(LocalDate.now())
                                    .build());
                            content.setContentId(f.getId());
                        } catch (IOException e) {
                            log.error("Could not store part file {}", content.getTitle(), e);
                        }
                    }
                }
            }
        }
    }
}
