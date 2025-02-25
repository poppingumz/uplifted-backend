package fontys.sem3.school.persistence.impl;

import fontys.sem3.school.persistence.StudentRepository;
import fontys.sem3.school.persistence.entity.StudentEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeStudentRepositoryImpl implements StudentRepository {
    private static long NEXT_ID = 1;
    private final List<StudentEntity> savedStudents;

    public FakeStudentRepositoryImpl() {
        this.savedStudents = new ArrayList<>();
    }

    @Override
    public boolean existsByPcn(long pcn) {
        return this.savedStudents
                .stream()
                .anyMatch(studentEntity -> studentEntity.getPcn().equals(pcn));
    }

    @Override
    public List<StudentEntity> findAllByCountryCode(String countryCode) {
        return this.savedStudents
                .stream()
                .filter(studentEntity -> studentEntity.getCountry().getCode().equals(countryCode))
                .toList();
    }

    @Override
    public StudentEntity save(StudentEntity student) {
        if (student.getId() == null) {
            student.setId(NEXT_ID);
            NEXT_ID++;
            this.savedStudents.add(student);
        }
        return student;
    }

    @Override
    public void deleteById(long studentId) {
        this.savedStudents.removeIf(studentEntity -> studentEntity.getId().equals(studentId));
    }

    @Override
    public List<StudentEntity> findAll() {
        return Collections.unmodifiableList(this.savedStudents);
    }

    @Override
    public Optional<StudentEntity> findById(long studentId) {
        return this.savedStudents.stream()
                .filter(studentEntity -> studentEntity.getId().equals(studentId))
                .findFirst();
    }
}
