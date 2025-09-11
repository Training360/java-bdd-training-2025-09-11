package courses;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseApplicationService courseApplicationService;

    private final CourseQueryService courseQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseView announceCourse(@RequestBody CourseAnnouncement announcement) {
        return courseApplicationService.announceCourse(announcement);
    }

    @GetMapping
    public List<CourseView> findCourses() {
        return courseQueryService.findAll();
    }

    @PostMapping("{code}/enrollments")
    public CourseView enroll(@PathVariable("code") String code, @RequestBody Applicant applicant) {
        return courseApplicationService.enroll(code, applicant.name());
    }

    @GetMapping("{code}/enrollments")
    public Set<String> enrollmentsOf(@PathVariable("code") String code) {
        return courseQueryService.enrollmentsOf(code);
    }

}
