package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.domain.StudentAnswer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public ResponseEntity<String> reviewAnswer(
            @PathVariable Long answerId,
            @RequestBody ReviewRequest request
    ) {
        try {
            answerService.reviewAnswer(answerId, request.getMarks(), request.getFeedback());
            return ResponseEntity.ok("Review submitted successfully");
        } catch (Exception e) {
            log.error("Review processing failed for answerId {}: {}", answerId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Review failed: " + e.getMessage());
        }
    }

    @Getter @Setter
    public static class ReviewRequest {
        private int marks;
        private String feedback;
    }
}
