package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.DeleteStudentUseCase;
import fontys.sem3.school.persistence.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteStudentUseCaseImpl implements DeleteStudentUseCase {
    private final StudentRepository studentRepository;

    @Override
    public void deleteStudent(long studentId) {
        this.studentRepository.deleteById(studentId);
    }
}
