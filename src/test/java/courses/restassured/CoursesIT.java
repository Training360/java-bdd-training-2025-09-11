package courses.restassured;


import courses.CourseAnnouncement;
import courses.CourseView;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesIT {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void initMockMvc() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.requestSpecification =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON);
    }

    @Test
    void announceCourse() {
        String code = "course-" + UUID.randomUUID();
        var course = with()
                .body(new CourseAnnouncement(code, "Java"))
                .post("/api/courses")
                .then()
                .statusCode(201)
                .extract().as(CourseView.class);
        assertEquals(code, course.code());
        assertEquals("Java", course.name());

        List<CourseView> courses = with()
                .get("/api/courses")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<>() {});

        assertThat(courses).extracting(CourseView::code).contains(code);
    }

}
