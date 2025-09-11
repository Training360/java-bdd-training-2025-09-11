package courses.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoursesPage {

    private final Page page;

    public void navigate(int port) {
        page.navigate("http://localhost:%d".formatted(port));
    }

    public void announce(String code, String name) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("code")).fill(code);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("name")).fill(name);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Announce")).click();
    }

    public void containsCourse(String code) {
        assertThat(page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(code))).isVisible();
    }
}
