package courses.playwrightcucumber;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import courses.playwright.CoursePage;
import courses.playwright.CoursesPage;
import groovy.util.logging.Slf4j;
import io.cucumber.java.After;
import io.cucumber.spring.ScenarioScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

@lombok.extern.slf4j.Slf4j
@Component
@ScenarioScope
@Slf4j
public class CoursesWorld {

    @LocalServerPort
    int port;

    Playwright playwright;
    Browser browser;
    Page page;

    private CoursePage coursePage;

    private CoursesPage coursesPage;

    @PostConstruct
    public void setUp() {
        log.info("Setting up playwright");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    public byte[] takeScreenshot() {
        return page.screenshot(new Page.ScreenshotOptions().setPath(null));
    }


    @PreDestroy
    public void tearDown() {
        log.info("Tearing down playwright");
        page.close();
        browser.close();
        playwright.close();
    }

    public CoursesPage toCoursesPage() {
        coursesPage = new CoursesPage(page);
        coursesPage.navigate(port);
        return coursesPage;
    }

    public CoursePage toCoursePage(String courseCode) {
        coursePage = new CoursePage(page);
        coursePage.navigate(port, courseCode);
        return coursePage;
    }

    public CoursePage getCoursePage() {
        return coursePage;
    }

    public CoursesPage getCoursesPage() {
        return coursesPage;
    }
}
