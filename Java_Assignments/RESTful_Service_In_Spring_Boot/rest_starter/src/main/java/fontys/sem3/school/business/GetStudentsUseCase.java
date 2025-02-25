package fontys.sem3.school.business;

import fontys.sem3.school.domain.GetAllStudentsRequest;
import fontys.sem3.school.domain.GetAllStudentsResponse;

public interface GetStudentsUseCase {
    GetAllStudentsResponse getStudents(GetAllStudentsRequest request);
}
