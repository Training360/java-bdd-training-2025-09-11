package courses.httpinterfacecucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import courses.Applicant;
import courses.CourseAnnouncement;
import courses.httpinterface.CourseRequestObject;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("courses")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "courses.httpinterfacecucumber")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CoursesIT {

    @LocalServerPort
    private int port;

    @Autowired
    RestClient.Builder restClientBuilder;

    CourseRequestObject client;

    String code;

    String result;

    @Before
    public void init() {
        var restClient = restClientBuilder
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
        var factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();
        client = factory.createClient(CourseRequestObject.class);
    }

    @Given("a course {string} with a limit of {int} enrollments")
    public void a_course_with_a_limit_of_enrollments(String name, Integer limit) {
        code = "course-" + java.util.UUID.randomUUID();
        client.announceCourse(new CourseAnnouncement(this.code, name, limit));
    }

    @Given("{int} applicants attempt to enroll in the course")
    public void applicants_attempt_to_enroll_in_the_course(Integer count) {
        for (int i = 1; i <= count; i++) {
            client.enroll(this.code, new Applicant("John Doe %d".formatted(i)));
        }
    }

    @When("one more applicant attempts to enroll")
    @SneakyThrows
    public void one_more_applicant_attempts_to_enroll() {
        try {
            client.enroll(code, new Applicant("John Doe Overflow"));
        }catch (HttpClientErrorException e) {
            ObjectMapper mapper = new ObjectMapper();
            ProblemDetail problem = mapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
            result = problem.getDetail();
        }
    }

    @Then("the applicamt should be rejected with a message {string}")
    public void the_applicamt_should_be_rejected_with_a_message(String message) {
        assertEquals(message, result);
    }
}
