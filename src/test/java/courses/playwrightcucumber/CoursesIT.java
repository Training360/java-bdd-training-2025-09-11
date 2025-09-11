package courses.playwrightcucumber;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import courses.playwright.CoursePage;
import courses.playwright.CoursesPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("courses")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "courses.playwrightcucumber")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoursesIT {

    @LocalServerPort
    int port;

    Playwright playwright;
    Browser browser;
    Page page;

    @Before
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @After
    public void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }

    private String code;

    private CoursePage coursePage;

    @Given("a course {string} with a limit of {int} enrollments")
    public void a_course_with_a_limit_of_enrollments(String name, Integer limit) {
        code = "course-" + UUID.randomUUID();
        CoursesPage coursesPage = new CoursesPage(page);
        coursesPage.navigate(port);
        coursesPage.announce(code, name, limit);
    }

    @Given("{int} applicants attempt to enroll in the course")
    public void applicants_attempt_to_enroll_in_the_course(Integer number) {
        coursePage = new CoursePage(page);
        coursePage.navigate(port, code);
        for (int i = 1; i <= number; i++) {
            coursePage.enroll("John Doe " + i);
        }
    }

    @When("one more applicant attempts to enroll")
    public void one_more_applicant_attempts_to_enroll() {
        coursePage = new CoursePage(page);
        coursePage.enroll("John Doe Overflow");
    }

    @Then("the applicamt should be rejected with a message {string}")
    public void the_applicamt_should_be_rejected_with_a_message(String message) {
        coursePage.gotAlert(message);
    }
}
