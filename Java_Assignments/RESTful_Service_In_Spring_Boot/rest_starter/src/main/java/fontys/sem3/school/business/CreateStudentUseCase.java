package fontys.sem3.school.business;

import fontys.sem3.school.domain.CreateStudentRequest;
import fontys.sem3.school.domain.CreateStudentResponse;

public interface CreateStudentUseCase {
    CreateStudentResponse createStudent(CreateStudentRequest request);
}
