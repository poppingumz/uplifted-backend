package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.business.impl.mapper.StudentAnswerMapper;
import fontys.s3.uplifted.domain.StudentAnswer;
import fontys.s3.uplifted.persistence.StudentAnswerRepository;
import fontys.s3.uplifted.persistence.entity.StudentAnswerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAnswerServiceImpl implements StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;

    @Override
    public void submitAnswer(StudentAnswer answer) {
        StudentAnswerEntity entity = StudentAnswerMapper.toEntity(answer);
        entity.setReviewed(false); // force default
        studentAnswerRepository.save(entity);
    }

    @Override
    public void reviewAnswer(Long answerId, int marks, String feedback) {
        StudentAnswerEntity entity = studentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        entity.setAwardedMarks(marks);
        entity.setMentorFeedback(feedback);
        entity.setReviewed(true);

        studentAnswerRepository.save(entity);
    }

    @Override
    public List<StudentAnswer> getPendingReviews() {
        return studentAnswerRepository.findByReviewedFalse().stream()
                .map(StudentAnswerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentAnswer> getAnswersByUser(Long userId) {
        return studentAnswerRepository.findByUserId(userId).stream()
                .map(StudentAnswerMapper::toDomain)
                .collect(Collectors.toList());
    }
}
