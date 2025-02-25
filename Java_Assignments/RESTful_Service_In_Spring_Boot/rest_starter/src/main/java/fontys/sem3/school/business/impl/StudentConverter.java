package fontys.sem3.school.business.impl;

import fontys.sem3.school.domain.Student;
import fontys.sem3.school.persistence.entity.StudentEntity;

final class StudentConverter {
    private StudentConverter() {
    }

    public static Student convert(StudentEntity student) {
        return Student.builder()
                .id(student.getId())
                .pcn(student.getPcn())
                .name(student.getName())
                .country(CountryConverter.convert(student.getCountry()))
                .build();
    }
}
