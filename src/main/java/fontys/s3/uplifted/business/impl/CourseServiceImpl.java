package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses()
                .stream()
                .map(CourseMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.getCourseById(id).map(CourseMapper::toDomain);
    }

    public Course createCourse(Course course) {
        CourseEntity entity = CourseMapper.toEntity(course);
        CourseEntity savedEntity = courseRepository.createCourse(entity);
        return CourseMapper.toDomain(savedEntity);
    }

    public Optional<Course> updateCourse(Long id, Course course) {
        CourseEntity entity = CourseMapper.toEntity(course);
        return courseRepository.updateCourse(id, entity).map(CourseMapper::toDomain);
    }

    public boolean deleteCourse(Long id) {
        return courseRepository.deleteCourse(id);
    }
}
