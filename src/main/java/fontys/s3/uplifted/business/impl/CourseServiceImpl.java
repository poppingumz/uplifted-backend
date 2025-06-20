package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.CoursePart;
import fontys.s3.uplifted.domain.CoursePartContent;
import fontys.s3.uplifted.domain.dto.CourseResponseDTO;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.*;
import fontys.s3.uplifted.websocket.NotificationWebSocketEndpoint;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;

    private static final String ENROLLED = "enrolledStudents";
    private static final int PAGE_SIZE = 6;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll().stream()
                .filter(CourseEntity::isPublished)
                .map(CourseMapper::toDomain)
                .toList();
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
                .toList();
    }

    @Transactional
    @Override
    public Course createCourse(Course course, List<MultipartFile> uploadedFiles) {
        UserEntity instructor = findInstructor(course.getInstructorId());
        CourseEntity saved = courseRepository.save(CourseMapper.toEntity(course, instructor));
        attachFilesToContents(saved, uploadedFiles, instructor);
        CourseEntity result = courseRepository.save(saved);

        NotificationMessage message = new NotificationMessage(
                result.getId(),
                result.getTitle() + " is now available!",
                result.getCategory().name()
        );

        sendNotifications(message);
        return CourseMapper.toDomain(result);
    }

    @Transactional
    @Override
    public Optional<Course> updateCourse(Long id, Course course, List<MultipartFile> uploadedFiles) {
        return courseRepository.findById(id).map(existing -> {
            UserEntity instructor = findInstructor(course.getInstructorId());
            updateBasicCourseInfo(existing, course, instructor);
            existing.getParts().clear();

            if (course.getParts() != null) {
                course.getParts().forEach(partDto -> {
                    CoursePartEntity partEntity = buildPartEntity(partDto, existing);
                    existing.getParts().add(partEntity);
                });
            }

            attachFilesToContents(existing, uploadedFiles, instructor);
            CourseEntity saved = courseRepository.save(existing);

            NotificationMessage message = new NotificationMessage(
                    saved.getId(),
                    "Course updated: " + saved.getTitle(),
                    saved.getCategory().name()
            );
            sendNotifications(message);

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
        var enrolledCourseIds = enrollmentRepository.findByUserId(userId).stream()
                .map(e -> e.getCourseId())
                .toList();

        return courseRepository.findAllById(enrolledCourseIds).stream()
                .map(CourseMapper::toDomain)
                .toList();
    }

    @Override
    public void enrollInCourse(Long courseId, String email) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        if (course.getEnrollmentLimit() > 0
                && enrollmentService.getEnrolledStudents(courseId).size() >= course.getEnrollmentLimit()) {
            throw new IllegalStateException("Course enrollment limit reached.");
        }

        enrollmentService.enrollStudent(courseId, user.getId());
        log.info("User {} enrolled in course {}", user.getId(), courseId);
    }

    @Override
    public void unenrollFromCourse(Long courseId, String email) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Course not found with ID: " + courseId));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + email));

        enrollmentService.unenrollStudent(courseId, user.getId());
        log.info("User {} unenrolled from course {}", user.getId(), course.getId());
    }



    private void attachFilesToContents(CourseEntity courseEntity, List<MultipartFile> uploadedFiles, UserEntity instructor) {
        if (courseEntity.getParts() == null || uploadedFiles == null) {
            log.warn("Course has no parts or no uploaded files, skipping file attachment.");
            return;
        }

        int idx = 0;
        for (CoursePartEntity part : courseEntity.getParts()) {
            if (part.getContents() == null) continue;

            for (CoursePartContentEntity content : part.getContents()) {
                if (content.getContentType() == ContentType.FILE && idx < uploadedFiles.size()) {
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

    private UserEntity findInstructor(Long instructorId) {
        return userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    private void updateBasicCourseInfo(CourseEntity existing, Course course, UserEntity instructor) {
        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setCategory(course.getCategory());
        existing.setEnrollmentLimit(course.getEnrollmentLimit());
        existing.setPublished(course.isPublished());
        existing.setInstructor(instructor);
    }

    private CoursePartEntity buildPartEntity(CoursePart partDto, CourseEntity course) {
        CoursePartEntity partEntity = new CoursePartEntity();
        partEntity.setTitle(partDto.getTitle());
        partEntity.setWeekNumber(partDto.getWeekNumber());
        partEntity.setSequence(partDto.getSequence());
        partEntity.setCourse(course);

        if (partDto.getContents() != null) {
            List<CoursePartContentEntity> contentEntities = partDto.getContents().stream()
                    .map(contentDto -> buildContentEntity(contentDto, partEntity))
                    .toList();
            partEntity.setContents(contentEntities);
        }

        return partEntity;
    }

    private CoursePartContentEntity buildContentEntity(CoursePartContent contentDto, CoursePartEntity partEntity) {
        CoursePartContentEntity content = new CoursePartContentEntity();
        content.setContentType(contentDto.getContentType());
        content.setContentId(contentDto.getContentType() == ContentType.VIDEO ? null : contentDto.getContentId());
        content.setTitle(contentDto.getTitle());
        content.setPart(partEntity);
        return content;
    }

    private void sendNotifications(NotificationMessage message) {
        NotificationWebSocketEndpoint.broadcast(message);
    }

    @Override
    public Page<CourseResponseDTO> getFilteredCourses(String title, String category, String sort, int page) {
        PageRequest pageReq = PageRequest.of(page, PAGE_SIZE);

        if ("popular".equalsIgnoreCase(sort)) {
            return courseRepository.findAll(popularSpec(title, category), pageReq)
                    .map(CourseMapper::toResponseDTO);
        }

        Sort sortOrder = Sort.unsorted();
        if ("newest".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.DESC, "id");
        } else if ("oldest".equalsIgnoreCase(sort)) {
            sortOrder = Sort.by(Sort.Direction.ASC, "id");
        }

        return courseRepository
                .findAll(baseSpec(title, category), PageRequest.of(page, PAGE_SIZE, sortOrder))
                .map(CourseMapper::toResponseDTO);
    }

    private Specification<CourseEntity> baseSpec(String title, String category) {
        return (root, query, cb) -> {
            root.fetch(ENROLLED, JoinType.LEFT);
            query.distinct(true);
            return buildFilters(root, cb, title, category);
        };
    }

    private Specification<CourseEntity> popularSpec(String title, String category) {
        return (root, query, cb) -> {
            // join so we can count
            var join = root.join(ENROLLED, JoinType.LEFT);
            query.groupBy(root.get("id"));
            query.orderBy(cb.desc(cb.count(join)));
            return buildFilters(root, cb, title, category);
        };
    }

    private Predicate buildFilters(Root<CourseEntity> root,
                                   CriteriaBuilder cb,
                                   String title,
                                   String category) {
        Predicate p = cb.isTrue(root.get("published"));
        if (title != null && !title.isBlank()) {
            p = cb.and(p,
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (category != null && !category.isBlank()) {
            p = cb.and(p,
                    cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }
        return p;
    }

}
