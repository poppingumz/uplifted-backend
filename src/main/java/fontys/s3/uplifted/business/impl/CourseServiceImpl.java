package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses()
                .stream()
                .map(courseEntity -> {
                    Course course = CourseMapper.toDomain(courseEntity);

                    userRepository.getUserById(courseEntity.getTeacherId()).ifPresent(user -> {
                        System.out.println("Instructor Name: " + user.getUsername());
                    });

                    return course;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.getCourseById(id).map(courseEntity -> {
            Course course = CourseMapper.toDomain(courseEntity);

            userRepository.getUserById(courseEntity.getTeacherId()).ifPresent(user -> {
                System.out.println("Instructor Name: " + user.getUsername());
            });

            return course;
        });
    }

    @Override
    public Course createCourse(Course course) {
        CourseEntity entity = CourseMapper.toEntity(course);
        entity.setTeacherId(course.getTeacherId());
        CourseEntity savedEntity = courseRepository.createCourse(entity);
        return CourseMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Course> updateCourse(Long id, Course course) {
        CourseEntity entity = CourseMapper.toEntity(course);
        return courseRepository.updateCourse(id, entity).map(CourseMapper::toDomain);
    }

    @Override
    public boolean deleteCourse(Long id) {
        return courseRepository.deleteCourse(id);
    }
}
