package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByUploaderId(Long userId);
    List<FileEntity> findByCourseId(Long courseId);
    List<FileEntity> findByNameContainingIgnoreCase(String name);
    List<FileEntity> findByUploadDateAfter(LocalDate date);
}

