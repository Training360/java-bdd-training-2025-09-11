package courses;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "courses")
public class Course {

    @Id
    private String code;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<String> enrollments = new HashSet<>();

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Course announce(CourseAnnouncement announcement) {
        return new Course(announcement.code(), announcement.name());
    }

    public void enroll(String applicant) {
        if (enrollments.contains(applicant)) {
            return;
        }
        enrollments.add(applicant);
    }

}
