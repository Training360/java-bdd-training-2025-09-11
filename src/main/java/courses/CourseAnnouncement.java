package courses;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CourseAnnouncement(
        @NotBlank
        String code,
        @NotBlank
        String name) {
}
