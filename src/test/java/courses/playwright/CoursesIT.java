package courses.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoursesIT {

    @LocalServerPort
    int port;

    static Playwright playwright;
    static Browser browser;
    Page page;

    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() {
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        page.close();
    }

    @Test
    void announceCourse() {
        CoursesPage coursesPage = new CoursesPage(page);
        coursesPage.navigate(port);
        String code = "course-" + UUID.randomUUID();
        coursesPage.announce(code, "Java", 0);
        coursesPage.containsCourse(code);
    }

    @Test
    void enrollToCourse() {
        CoursesPage coursesPage = new CoursesPage(page);
        coursesPage.navigate(port);
        String code = "course-" + UUID.randomUUID();
        coursesPage.announce(code, "Java", 0);

        CoursePage coursePage = new CoursePage(page);
        coursePage.navigate(port, code);
        coursePage.enroll("Attendee");
        coursePage.gotStatus("Enrolled");
    }
}
