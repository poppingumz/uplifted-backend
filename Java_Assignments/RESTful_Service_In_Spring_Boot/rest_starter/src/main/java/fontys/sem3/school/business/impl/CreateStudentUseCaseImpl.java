package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.CountryIdValidator;
import fontys.sem3.school.business.CreateStudentUseCase;
import fontys.sem3.school.business.exception.PcnAlreadyExistsException;
import fontys.sem3.school.domain.CreateStudentRequest;
import fontys.sem3.school.domain.CreateStudentResponse;
import fontys.sem3.school.persistence.CountryRepository;
import fontys.sem3.school.persistence.StudentRepository;
import fontys.sem3.school.persistence.entity.CountryEntity;
import fontys.sem3.school.persistence.entity.StudentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateStudentUseCaseImpl implements CreateStudentUseCase {
    private final StudentRepository studentRepository;
    private final CountryRepository countryRepository;
    private final CountryIdValidator countryIdValidator;

    @Override
    public CreateStudentResponse createStudent(CreateStudentRequest request) {
        if (studentRepository.existsByPcn(request.getPcn())) {
            throw new PcnAlreadyExistsException();
        }

        countryIdValidator.validateId(request.getCountryId());

        StudentEntity savedStudent = saveNewStudent(request);

        return CreateStudentResponse.builder()
                .studentId(savedStudent.getId())
                .build();
    }

    private StudentEntity saveNewStudent(CreateStudentRequest request) {
        CountryEntity countryEntity = countryRepository.findById(request.getCountryId());

        StudentEntity newStudent = StudentEntity.builder()
                .country(countryEntity)
                .name(request.getName())
                .pcn(request.getPcn())
                .build();
        return studentRepository.save(newStudent);
    }
}
