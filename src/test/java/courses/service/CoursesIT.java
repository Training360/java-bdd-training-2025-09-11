package courses.service;

import courses.CourseAnnouncement;
import courses.CourseApplicationService;
import courses.CourseQueryService;
import courses.CourseView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CoursesIT {

    @Autowired
    CourseApplicationService applicationService;

    @Autowired
    CourseQueryService queryService;

    @Test
    void announce() {
        String code = "course-" + UUID.randomUUID();
        applicationService.announceCourse(new CourseAnnouncement(code, "Java"));
        List<CourseView> courses = queryService.findAll();
        assertThat(courses).extracting(CourseView::code).contains(code);
    }
}
