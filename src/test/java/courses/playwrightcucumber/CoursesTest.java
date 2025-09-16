package courses.playwrightcucumber;

import courses.playwright.CoursePage;
import courses.playwright.CoursesPage;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("courses")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "courses.playwrightcucumber")
@ConfigurationParameter(key = "cucumber.plugin", value = "pretty, html:target/cucumber.html")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoursesTest {

    @Autowired
    CoursesWorld coursesWorld;

    private String code;

    @After
    public void doScreenshot(Scenario scenario) {
        log.info("Scenario {} finished, failed: {}", scenario.getName(), scenario.isFailed());
        if (scenario.isFailed()) {
            byte[] screenshot = coursesWorld.takeScreenshot();
            scenario.attach(screenshot, "image/png", "name");
        }
    }

    @Given("a course {string} with a limit of {int} enrollments")
    public void a_course_with_a_limit_of_enrollments(String name, Integer limit) {
        code = "course-" + UUID.randomUUID();
        CoursesPage page = coursesWorld.toCoursesPage();
        page.announce(code, name, limit);
    }

    @Given("{int} applicants attempt to enroll in the course")
    public void applicants_attempt_to_enroll_in_the_course(Integer number) {
        CoursePage page = coursesWorld.toCoursePage(code);
        for (int i = 1; i <= number; i++) {
            page.enroll("John Doe " + i);
        }
    }

    @When("one more applicant attempts to enroll")
    public void one_more_applicant_attempts_to_enroll() {
        coursesWorld.getCoursePage().enroll("John Doe Overflow");
    }

    @Then("the applicamt should be rejected with a message {string}")
    public void the_applicamt_should_be_rejected_with_a_message(String message) {
        coursesWorld.getCoursePage().gotAlert(message);
    }
}
