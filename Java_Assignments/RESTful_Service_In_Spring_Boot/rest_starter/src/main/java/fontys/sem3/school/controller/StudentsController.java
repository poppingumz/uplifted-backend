package fontys.sem3.school.controller;

import fontys.sem3.school.business.*;
import fontys.sem3.school.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentsController {
    private final GetStudentUseCase getStudentUseCase;
    private final GetStudentsUseCase getStudentsUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final CreateStudentUseCase createStudentUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable(value = "id") final long id) {
        final Optional<Student> studentOptional = getStudentUseCase.getStudent(id);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(studentOptional.get());
    }

    @GetMapping
    public ResponseEntity<GetAllStudentsResponse> getAllStudents(@RequestParam(value = "country", required = false) String countryCode) {
        GetAllStudentsRequest request = GetAllStudentsRequest.builder().countryCode(countryCode).build();
        GetAllStudentsResponse response = getStudentsUseCase.getStudents(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int studentId) {
        deleteStudentUseCase.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<CreateStudentResponse> createStudent(@RequestBody @Valid CreateStudentRequest request) {
        CreateStudentResponse response = createStudentUseCase.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable("id") long id,
                                              @RequestBody @Valid UpdateStudentRequest request) {
        request.setId(id);
        updateStudentUseCase.updateStudent(request);
        return ResponseEntity.noContent().build();
    }
}
