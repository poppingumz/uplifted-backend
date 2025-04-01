package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeCourseRepositoryImpl implements CourseRepository {
    private final Map<Long, CourseEntity> courses = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        createCourse(CourseEntity.builder()
                .title("Java Spring Boot")
                .description("Build REST APIs")
                .instructorName("Alice Johnson")
                .enrolledStudentIds(Set.of(1001L, 1002L))
                .build());

        createCourse(CourseEntity.builder()
                .title("React Essentials")
                .description("Learn React basics")
                .instructorName("Bob Smith")
                .enrolledStudentIds(Set.of())
                .build());

        createCourse(CourseEntity.builder()
                .title("Docker Deep Dive")
                .description("Understand Docker and containers")
                .instructorName("Charlie Lee")
                .enrolledStudentIds(Set.of(1003L))
                .build());
    }


    public List<CourseEntity> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Optional<CourseEntity> getCourseById(Long id) {
        return Optional.ofNullable(courses.get(id));
    }

    public CourseEntity createCourse(CourseEntity course) {
        long newId = idGenerator.getAndIncrement();
        course.setId(newId);
        courses.put(newId, course);
        return course;
    }

    public Optional<CourseEntity> updateCourse(Long id, CourseEntity updatedCourse) {
        if (!courses.containsKey(id)) {
            return Optional.empty();
        }
        updatedCourse.setId(id);
        courses.put(id, updatedCourse);
        return Optional.of(updatedCourse);
    }

    public boolean deleteCourse(Long id) {
        return courses.remove(id) != null;
    }
}
