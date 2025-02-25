package fontys.sem3.school.business;

import fontys.sem3.school.domain.Student;

import java.util.Optional;

public interface GetStudentUseCase {
    Optional<Student> getStudent(long studentId);
}
