package courses.notgherkin;

import courses.CourseApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoursesIT {

    @Autowired
    CourseApplicationService courseApplicationService;

    BusinessFlow flow;

    @BeforeEach
    void setUp() {
        flow = new BusinessFlow(courseApplicationService);
    }

    @Test
    void limitEnrollments() {
        // Given
        flow.courseExists("Introduction to Testing", 3);
        flow.enrolledApplicants(3);
        // When
        flow.applicantAttemptsToEnroll("John Doe Overflow");
        // Then
        flow.applicantShouldBeRejectedWithMessage("Enrollment limit reached: 3");
    }
}
