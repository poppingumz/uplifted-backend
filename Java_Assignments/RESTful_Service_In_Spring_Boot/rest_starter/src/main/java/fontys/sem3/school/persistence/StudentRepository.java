package fontys.sem3.school.persistence;

import fontys.sem3.school.persistence.entity.StudentEntity;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    boolean existsByPcn(long pcn);

    List<StudentEntity> findAllByCountryCode(String countryCode);

    StudentEntity save(StudentEntity student);

    void deleteById(long studentId);

    List<StudentEntity> findAll();

    Optional<StudentEntity> findById(long studentId);
}
