package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.domain.StudentAnswer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class MentorReviewController {

    private final StudentAnswerService answerService;

    @GetMapping("/pending")
    public ResponseEntity<List<StudentAnswer>> getPendingReviews() {
        return ResponseEntity.ok(answerService.getPendingReviews());
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<Void> reviewAnswer(
            @PathVariable Long answerId,
            @RequestBody ReviewRequest request
    ) {
        answerService.reviewAnswer(answerId, request.getMarks(), request.getFeedback());
        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    public static class ReviewRequest {
        private int marks;
        private String feedback;
    }
}
