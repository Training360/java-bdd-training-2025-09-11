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

    @Column(name = "seat_limit")
    private int limit;

    @ElementCollection(fetch = FetchType.EAGER)
    private final Set<String> enrollments = new HashSet<>();

    public Course(String code, String name, int limit) {
        this.code = code;
        this.name = name;
        this.limit = limit;
    }

    public static Course announce(CourseAnnouncement announcement) {
        return new Course(announcement.code(), announcement.name(), announcement.limit());
    }

    public void enroll(String applicant) {
        if (enrollments.contains(applicant)) {
            return;
        }
        if (limit > 0 && enrollments.size() >= limit) {
            throw new IllegalStateException("Enrollment limit reached: %d".formatted(limit));
        }
        enrollments.add(applicant);
    }

}
