package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


    public List<Course> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toDomain);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId)
                .stream()
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Course createCourse(Course course) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity instructor = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        CourseEntity entity = CourseMapper.toEntity(course, instructor);
        CourseEntity saved = courseRepository.save(entity);
        return CourseMapper.toDomain(saved);
    }


    public Optional<Course> updateCourse(Long id, Course course) {
        return courseRepository.findById(id).map(existing -> {
            UserEntity instructor = userRepository.findById(course.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + course.getInstructorId()));

            CourseEntity entity = CourseMapper.toEntity(course, instructor);
            entity.setId(id);
            CourseEntity updated = courseRepository.save(entity);
            return CourseMapper.toDomain(updated);
        });
    }

    public boolean deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course with ID " + id + " not found.");
        }
        courseRepository.deleteById(id);
        return true;
    }

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

        if (course.getEnrollmentLimit() > 0 &&
                course.getEnrolledStudents().size() >= course.getEnrollmentLimit()) {
            throw new RuntimeException("Course enrollment limit reached.");
        }

        course.getEnrolledStudents().add(user);
        courseRepository.save(course);
    }
}

