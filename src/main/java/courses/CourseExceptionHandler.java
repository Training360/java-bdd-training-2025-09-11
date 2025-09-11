package courses;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handle(IllegalStateException e) {
        var problemDetail = ProblemDetail.forStatus(409);
        problemDetail.setTitle("Conflict");
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }
}
