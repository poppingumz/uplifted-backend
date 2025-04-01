package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        try {
            return courseRepository.getAllCourses()
                    .stream()
                    .map(CourseMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch courses", e);
            throw new RuntimeException("Unable to retrieve courses at this time.");
        }
    }

    public Optional<Course> getCourseById(Long id) {
        try {
            return courseRepository.getCourseById(id)
                    .map(CourseMapper::toDomain);
        } catch (Exception e) {
            log.error("Error retrieving course with ID {}", id, e);
            throw new RuntimeException("Unable to retrieve the course.");
        }
    }

    public Course createCourse(Course course) {
        try {
            CourseEntity entity = CourseMapper.toEntity(course);
            CourseEntity savedEntity = courseRepository.createCourse(entity);
            return CourseMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Failed to create course: {}", course.getTitle(), e);
            throw new RuntimeException("Unable to create course.");
        }
    }

    public Optional<Course> updateCourse(Long id, Course course) {
        try {
            if (!courseRepository.getCourseById(id).isPresent()) {
                log.warn("Attempted to update non-existent course with ID {}", id);
                throw new CourseNotFoundException("Course with ID " + id + " not found.");
            }

            CourseEntity entity = CourseMapper.toEntity(course);
            return courseRepository.updateCourse(id, entity)
                    .map(CourseMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to update course with ID {}", id, e);
            throw new RuntimeException("Unable to update course.");
        }
    }

    public boolean deleteCourse(Long id) {
        try {
            boolean deleted = courseRepository.deleteCourse(id);
            if (!deleted) {
                log.warn("Course with ID {} not found for deletion", id);
                throw new CourseNotFoundException("Course with ID " + id + " not found.");
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to delete course with ID {}", id, e);
            throw new RuntimeException("Unable to delete course.");
        }
    }
}
