package fontys.sem3.school.business.impl;

import fontys.sem3.school.business.GetStudentsUseCase;
import fontys.sem3.school.domain.GetAllStudentsRequest;
import fontys.sem3.school.domain.GetAllStudentsResponse;
import fontys.sem3.school.domain.Student;
import fontys.sem3.school.persistence.StudentRepository;
import fontys.sem3.school.persistence.entity.StudentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class GetStudentsUseCaseImpl implements GetStudentsUseCase {
    private StudentRepository studentRepository;

    @Override
    public GetAllStudentsResponse getStudents(final GetAllStudentsRequest request) {
        List<StudentEntity> results;
        if (StringUtils.hasText(request.getCountryCode())) {
            results = studentRepository.findAllByCountryCode(request.getCountryCode());
        } else {
            results = studentRepository.findAll();
        }

        final GetAllStudentsResponse response = new GetAllStudentsResponse();
        List<Student> students = results
                .stream()
                .map(StudentConverter::convert)
                .toList();
        response.setStudents(students);

        return response;
    }
}
