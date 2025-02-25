package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.GetStudentUseCase;
import fontys.sem3.school.domain.Student;
import fontys.sem3.school.persistence.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetStudentUseCaseImpl implements GetStudentUseCase {

    private StudentRepository studentRepository;

    @Override
    public Optional<Student> getStudent(long studentId) {
        return studentRepository.findById(studentId).map(StudentConverter::convert);
    }
}
