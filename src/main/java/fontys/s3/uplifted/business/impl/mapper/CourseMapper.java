package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.CoursePart;
import fontys.s3.uplifted.domain.CoursePartContent;
import fontys.s3.uplifted.domain.dto.*;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.persistence.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class CourseMapper {

    private CourseMapper() {}

    // ======== FROM ENTITY TO DOMAIN ========
    public static Course toDomain(CourseEntity entity) {
        return Course.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .enrollmentLimit(entity.getEnrollmentLimit())
                .published(entity.isPublished())
                .instructorId(entity.getInstructor().getId())
                .imageData(entity.getImageData())
                .parts(toPartDomainFromEntity(entity.getParts()))
                .build();
    }

    private static List<CoursePart> toPartDomainFromEntity(List<CoursePartEntity> partEntities) {
        if (partEntities == null) return new ArrayList<>();
        return partEntities.stream()
                .map(part -> CoursePart.builder()
                        .title(part.getTitle())
                        .sequence(part.getSequence())
                        .weekNumber(part.getWeekNumber())
                        .contents(toContentDomainFromEntity(part.getContents()))
                        .build())
                .toList();
    }

    private static List<CoursePartContent> toContentDomainFromEntity(List<CoursePartContentEntity> contentEntities) {
        if (contentEntities == null) return new ArrayList<>();
        return contentEntities.stream().map(c -> CoursePartContent.builder()
                .title(c.getTitle())
                .contentId(c.getContentId())
                .contentType(c.getContentType())
                .build()).toList();
    }

    // ======== FROM DTO TO DOMAIN ========
    public static Course toDomain(CourseDTO dto) {
        return Course.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .enrollmentLimit(dto.getEnrollmentLimit())
                .published(dto.isPublished())
                .instructorId(dto.getInstructorId())
                .parts(toPartDomain(dto.getParts()))
                .build();
    }

    private static List<CoursePart> toPartDomain(List<CoursePartDTO> partDTOs) {
        if (partDTOs == null) return new ArrayList<>();
        return IntStream.range(0, partDTOs.size())
                .mapToObj(i -> CoursePart.builder()
                        .title(partDTOs.get(i).getTitle())
                        .sequence(partDTOs.get(i).getSequence() != null ? partDTOs.get(i).getSequence() : i + 1)
                        .weekNumber(partDTOs.get(i).getWeekNumber())
                        .contents(toContentDomain(partDTOs.get(i).getContents()))
                        .build())
                .toList();
    }

    private static List<CoursePartContent> toContentDomain(List<CoursePartContentDTO> contentDTOs) {
        if (contentDTOs == null) return new ArrayList<>();
        return contentDTOs.stream().map(c -> CoursePartContent.builder()
                .title(c.getTitle())
                .contentId(c.getContentId())
                .contentType(ContentType.fromString(c.getContentType()))
                .build()).toList();
    }

    // ======== FROM DOMAIN TO RESPONSE DTO ========
    public static CourseResponseDTO toResponseDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .enrollmentLimit(course.getEnrollmentLimit())
                .published(course.isPublished())
                .instructorId(course.getInstructorId())
                .imageData(course.getImageData())
                .parts(toPartDTO(course.getParts()))
                .build();
    }

    private static List<CoursePartDTO> toPartDTO(List<CoursePart> parts) {
        if (parts == null) return new ArrayList<>();
        return parts.stream().map(part -> CoursePartDTO.builder()
                .title(part.getTitle())
                .sequence(part.getSequence())
                .weekNumber(part.getWeekNumber())
                .contents(toContentDTO(part.getContents()))
                .build()).toList();
    }

    private static List<CoursePartContentDTO> toContentDTO(List<CoursePartContent> contents) {
        if (contents == null) return new ArrayList<>();
        return contents.stream().map(c -> CoursePartContentDTO.builder()
                .title(c.getTitle())
                .contentType(c.getContentType().name())
                .contentId(c.getContentId())
                .build()).toList();
    }

    // ======== FROM DOMAIN TO ENTITY (FOR DB PERSISTENCE) ========
    public static CourseEntity toEntity(Course course, UserEntity instructor) {
        CourseEntity entity = CourseEntity.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .enrollmentLimit(course.getEnrollmentLimit())
                .published(course.isPublished())
                .imageData(course.getImageData())
                .instructor(instructor)
                .build();

        if (course.getParts() != null) {
            List<CoursePartEntity> parts = toPartEntity(course.getParts(), entity);
            entity.setParts(parts);
        }

        return entity;
    }

    private static List<CoursePartEntity> toPartEntity(List<CoursePart> parts, CourseEntity course) {
        if (parts == null) return new ArrayList<>();
        return IntStream.range(0, parts.size())
                .mapToObj(i -> {
                    CoursePart part = parts.get(i);
                    CoursePartEntity entity = CoursePartEntity.builder()
                            .title(part.getTitle())
                            .sequence(part.getSequence() > 0 ? part.getSequence() : i + 1)
                            .weekNumber(part.getWeekNumber())
                            .course(course)
                            .build();

                    List<CoursePartContentEntity> contentEntities = toContentEntity(part.getContents(), entity);
                    entity.setContents(contentEntities);

                    return entity;
                }).toList();
    }

    private static List<CoursePartContentEntity> toContentEntity(List<CoursePartContent> contents, CoursePartEntity part) {
        if (contents == null) return new ArrayList<>();
        return contents.stream().map(c -> CoursePartContentEntity.builder()
                .title(c.getTitle())
                .contentId(c.getContentId())
                .contentType(c.getContentType())
                .part(part)
                .build()).toList();
    }
}
