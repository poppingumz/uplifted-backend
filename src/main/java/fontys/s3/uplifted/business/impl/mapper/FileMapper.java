package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.File;
import fontys.s3.uplifted.persistence.entity.FileEntity;

public final class FileMapper {

    private FileMapper() {}

    public static File toDomain(FileEntity entity) {
        return File.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .data(entity.getData())
                .build();
    }

    public static FileEntity toEntity(File file) {
        return FileEntity.builder()
                .id(file.getId())
                .name(file.getName())
                .type(file.getType())
                .data(file.getData())
                .build();
    }
}
