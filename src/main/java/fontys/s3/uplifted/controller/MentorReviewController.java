package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.domain.StudentAnswer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentor_review")
@RequiredArgsConstructor
public class MentorReviewController {

    private final StudentAnswerService answerService;

    @GetMapping("/pending")
    public ResponseEntity<List<StudentAnswer>> getPendingReviews() {
        return ResponseEntity.ok(answerService.getPendingReviews());
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<?> reviewAnswer(
            @PathVariable Long answerId,
            @RequestBody ReviewRequest request
    ) {
        try {
            answerService.reviewAnswer(answerId, request.getMarks(), request.getFeedback());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @Getter @Setter
    public static class ReviewRequest {
        private int marks;
        private String feedback;
    }
}
