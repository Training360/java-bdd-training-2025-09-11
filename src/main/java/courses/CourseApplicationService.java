package courses;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseApplicationService {

    private final CourseRepository courseRepository;

    public CourseView announceCourse(CourseAnnouncement announcement) {
        Optional<Course> existing = courseRepository.findById(announcement.code());
        if (existing.isPresent()) {
            return toView(existing.get());
        }

        Course course = Course.announce(announcement);
        course = courseRepository.save(course);
        return toView(course);
    }

    @Transactional
    public CourseView enroll(String code, String applicant) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
        course.enroll(applicant);
        return toView(course);
    }

    private CourseView toView(Course course) {
        return new CourseView(course.getCode(), course.getName());
    }
}
