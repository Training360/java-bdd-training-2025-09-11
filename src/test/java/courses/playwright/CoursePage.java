package courses.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.RequiredArgsConstructor;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@RequiredArgsConstructor
public class CoursePage {

    private final Page page;

    public void navigate(int port, String courseCode) {
        page.navigate("http://localhost:%d/course?code=%s".formatted(port, courseCode));
    }

    public void enroll(String attendee) {
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("name")).fill(attendee);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enroll")).click();
    }

    public void gotStatus(String text) {
        //assertThat(page.getByRole(AriaRole.STATUS, new Page.GetByRoleOptions().setName(text))).isVisible();
        System.out.println(page.getByRole(AriaRole.STATUS).count());
        assertThat(page.locator("div").filter(new Locator.FilterOptions().setHasText(text))).isVisible();
    }

    public void gotAlert(String text) {
        assertThat(page.locator("div").filter(new Locator.FilterOptions().setHasText(text))).isVisible();
    }
}
