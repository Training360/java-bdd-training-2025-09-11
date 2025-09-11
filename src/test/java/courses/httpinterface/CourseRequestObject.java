package courses.httpinterface;


import courses.Applicant;
import courses.CourseAnnouncement;
import courses.CourseView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Set;

@HttpExchange("/api/courses")
public interface CourseRequestObject {

    @PostExchange
    CourseView announceCourse(@RequestBody CourseAnnouncement announcement);

    @GetExchange
    List<CourseView> findCourses();

    @PostExchange("{code}/enrollments")
    CourseView enroll(@PathVariable("code") String code, @RequestBody Applicant applicant);

    @GetExchange("{code}/enrollments")
    Set<String> enrollmentsOf(@PathVariable("code") String code);
}
