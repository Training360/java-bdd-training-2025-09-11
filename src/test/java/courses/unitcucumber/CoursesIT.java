package courses.unitcucumber;

import courses.Course;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Ez csak akkor fut, ha nincs a cucumber-spring a classpath-on
//@Suite
//@IncludeEngines("cucumber")
//@SelectPackages("courses")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "unit.unitcucumber")
public class CoursesIT {

    Course course;

    Exception exception;

    @Given("a course {string} with a limit of {int} enrollments")
    public void a_course_with_a_limit_of_enrollments(String name, Integer limit) {
        course = new Course(UUID.randomUUID().toString(), name, limit);
    }

    @Given("{int} applicants attempt to enroll in the course")
    public void applicants_attempt_to_enroll_in_the_course(Integer number) {
        for (int i = 1; i <= number; i++) {
            course.enroll("John Doe " + i);
        }
    }

    @When("one more applicant attempts to enroll")
    public void one_more_applicant_attempts_to_enroll() {
        try {
            course.enroll("John Doe Overflow");
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the applicamt should be rejected with a message {string}")
    public void the_applicamt_should_be_rejected_with_a_message(String message) {
        assertEquals(message, exception.getMessage());
    }
}
