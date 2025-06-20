    package fontys.s3.uplifted.controller;

    import fontys.s3.uplifted.business.QuizService;
    import fontys.s3.uplifted.domain.Quiz;
    import fontys.s3.uplifted.domain.dto.QuizSubmissionDTO;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Slf4j
    @RestController
    @RequestMapping("/api/quizzes")
    public class QuizController {

        private final QuizService quizService;

        public QuizController(QuizService quizService) {
            this.quizService = quizService;
        }

        @PostMapping
        public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
            try {
                Quiz created = quizService.createQuiz(quiz);
                return ResponseEntity.ok(created);
            } catch (Exception e) {
                log.error("Failed to create quiz for course ID {}", quiz.getCourseId(), e);
                return ResponseEntity.internalServerError().build();
            }
        }

        @GetMapping("/{id}")
        public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
            try {
                return quizService.getQuizById(id)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> {
                            log.warn("Quiz with ID {} not found", id);
                            return ResponseEntity.notFound().build();
                        });
            } catch (Exception e) {
                log.error("Error retrieving quiz with ID {}", id, e);
                return ResponseEntity.internalServerError().build();
            }
        }

        @GetMapping("/course/{courseId}")
        public ResponseEntity<List<Quiz>> getQuizzesByCourseId(@PathVariable Long courseId) {
            try {
                List<Quiz> quizzes = quizService.getQuizzesByCourseId(courseId);
                return ResponseEntity.ok(quizzes);
            } catch (Exception e) {
                log.error("Failed to get quizzes for course ID {}", courseId, e);
                return ResponseEntity.internalServerError().build();
            }
        }

        @GetMapping("/creator/{creatorId}")
        public ResponseEntity<List<Quiz>> getQuizzesByCreatorId(@PathVariable Long creatorId) {
            try {
                List<Quiz> quizzes = quizService.getQuizzesByCreatorId(creatorId);
                return ResponseEntity.ok(quizzes);
            } catch (Exception e) {
                log.error("Failed to get quizzes for creator ID {}", creatorId, e);
                return ResponseEntity.internalServerError().build();
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
            try {
                Quiz updated = quizService.updateQuiz(id, quiz);
                return ResponseEntity.ok(updated);
            } catch (RuntimeException e) {
                if (e.getMessage().contains("not authorized")) {
                    return ResponseEntity.status(403).build();
                }
                log.error("Failed to update quiz with ID {}", id, e);
                return ResponseEntity.internalServerError().build();
            }
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
            try {
                quizService.deleteQuiz(id);
                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                log.error("Failed to delete quiz with ID {}", id, e);
                return ResponseEntity.internalServerError().build();
            }
        }

        @PostMapping("/submit")
        public ResponseEntity<Void> submitQuizResult(@RequestBody QuizSubmissionDTO dto) {
            quizService.saveQuizResult(dto.getQuizId(), dto.getUserId(), dto.getScore(), dto.isPassed());
            return ResponseEntity.ok().build();
        }

    }
