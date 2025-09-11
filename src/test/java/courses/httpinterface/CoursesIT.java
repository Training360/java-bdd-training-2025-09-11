package courses.httpinterface;

import courses.CourseAnnouncement;
import courses.CourseView;
import courses.playwright.CoursesPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoursesIT {

    @LocalServerPort
    private int port;

    @Autowired
    RestClient.Builder restClientBuilder;

    CourseRequestObject client;

    @BeforeEach
    void setupClient() {
        var restClient = restClientBuilder
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
        var factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();
        client = factory.createClient(CourseRequestObject.class);
    }

    @Test
    void announceCourse() {
        String code = "course-" + UUID.randomUUID();
        var course = client.announceCourse(new CourseAnnouncement(code, "Java"));
        assert course.code().equals(code);

        var courses = client.findCourses();
        assertThat(courses).extracting(CourseView::code).contains(code);
    }

}
