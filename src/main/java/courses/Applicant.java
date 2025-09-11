package courses;

import jakarta.validation.constraints.NotBlank;

public record Applicant(
        @NotBlank
        String name) {
}
