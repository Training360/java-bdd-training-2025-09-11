package courses.notgherkin;

import courses.CourseAnnouncement;
import courses.CourseApplicationService;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class BusinessFlow {

    private final CourseApplicationService courseApplicationService;

    private String code;

    private Exception exception;

    public void courseExists(String name, int limit) {
        code = java.util.UUID.randomUUID().toString();
        courseApplicationService.announceCourse(new CourseAnnouncement(code, name, limit));
    }

    public void enrolledApplicants(int numberOfApplicants) {
        for (int i = 1; i <= numberOfApplicants; i++) {
            courseApplicationService.enroll(code, "John Doe " + i);
        }
    }


    public void applicantAttemptsToEnroll(String name) {
        try {
            courseApplicationService.enroll(code, name);
        }catch (Exception e) {
            this.exception = e;
        }
    }


    public void applicantShouldBeRejectedWithMessage(String message) {
        assertTrue(exception != null && exception.getMessage().equals(message));
    }
}
