package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeCourseRepositoryImpl implements CourseRepository {
    private final Map<Long, CourseEntity> courses = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

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
