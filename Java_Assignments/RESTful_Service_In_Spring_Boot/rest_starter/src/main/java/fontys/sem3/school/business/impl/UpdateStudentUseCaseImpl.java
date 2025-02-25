package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.CountryIdValidator;
import fontys.sem3.school.business.UpdateStudentUseCase;
import fontys.sem3.school.business.exception.InvalidStudentException;
import fontys.sem3.school.domain.UpdateStudentRequest;
import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.StudentRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import fontys.sem3.school.persistence.entity.StudentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateStudentUseCaseImpl implements UpdateStudentUseCase {
    private final StudentRepository studentRepository;
    private final CountryRepository countryRepository;
    private final CountryIdValidator countryIdValidator;

    @Override
    public void updateStudent(UpdateStudentRequest request) {
        Optional<StudentEntity> studentOptional = studentRepository.findById(request.getId());
        if (studentOptional.isEmpty()) {
            throw new InvalidStudentException("STUDENT_ID_INVALID");
        }

        countryIdValidator.validateId(request.getCountryId());

        StudentEntity student = studentOptional.get();
        updateFields(request, student);
    }

    private void updateFields(UpdateStudentRequest request, StudentEntity student) {
        CountryEntity countryEntity = countryRepository.findById(request.getCountryId());
        student.setCountry(countryEntity);
        student.setName(request.getName());

        studentRepository.save(student);
    }
}
