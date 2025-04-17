package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.domain.StudentAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class StudentAnswerController {

    private final StudentAnswerService answerService;

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@RequestBody StudentAnswer answer) {
        answerService.submitAnswer(answer);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{userId}")
    public ResponseEntity<List<StudentAnswer>> getAnswersByStudent(@PathVariable Long userId) {
        return ResponseEntity.ok(answerService.getAnswersByUser(userId));
    }
}
