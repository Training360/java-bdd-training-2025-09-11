package courses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CourseApplicationService courseApplicationService;

    private final CourseQueryService courseQueryService;

    @GetMapping("/")
    public ModelAndView findAll(@RequestParam(name = "msg", required = false) String message) {
        ModelAndView modelAndView = new ModelAndView("courses");
        List<CourseView> courses = courseQueryService.findAll();
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @PostMapping("/announce")
    public String announce(@Valid CourseAnnouncement courseAnnouncement, RedirectAttributes redirectAttributes) {
        courseApplicationService.announceCourse(courseAnnouncement);
        return "redirect:/?msg=Announced";
    }

    @GetMapping("/course")
    public ModelAndView courseDetail(@RequestParam("code") String code, @RequestParam(name = "msg", required = false) String message, @RequestParam(name = "role", required = false) String role) {
        ModelAndView modelAndView = new ModelAndView("course");
        CourseView course = courseQueryService.findOne(code);
        Set<String> enrollments = courseQueryService.enrollmentsOf(code);
        modelAndView.addObject("course", course);
        modelAndView.addObject("enrollments", enrollments);
        modelAndView.addObject("enrollForm", new EnrollForm(code, ""));
        modelAndView.addObject("message", message);
        modelAndView.addObject("role", role);
        return modelAndView;
    }

    @PostMapping("/enroll")
    public String enroll(@Valid EnrollForm enrollForm, RedirectAttributes redirectAttributes) {
        try {
            courseApplicationService.enroll(enrollForm.courseCode(), enrollForm.name());
            redirectAttributes.addAttribute("msg", "Enrolled");
            redirectAttributes.addAttribute("role", "status");
        } catch (IllegalStateException e) {
            redirectAttributes.addAttribute("msg", e.getMessage());
            redirectAttributes.addAttribute("role", "alert");
        }
        redirectAttributes.addAttribute("code", enrollForm.courseCode());
        return "redirect:/course?msg={msg}&code={code}";
    }
}
