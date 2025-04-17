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

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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

    public Course createCourse(Course course) {
        CourseEntity entity = CourseMapper.toEntity(course);
        CourseEntity saved = courseRepository.save(entity);
        return CourseMapper.toDomain(saved);
    }

    public Optional<Course> updateCourse(Long id, Course course) {
        return courseRepository.findById(id).map(existing -> {
            CourseEntity entity = CourseMapper.toEntity(course);
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
}

