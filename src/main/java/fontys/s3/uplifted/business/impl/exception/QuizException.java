package fontys.s3.uplifted.business.impl.exception;

public class QuizException extends RuntimeException {
    public QuizException(String message, UnauthorizedAccessException e) {
        super(message);
    }
}
