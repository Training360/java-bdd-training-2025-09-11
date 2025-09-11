package courses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseQueryService {

    private final CourseRepository courseRepository;

    public List<CourseView> findAll() {
        return courseRepository.findAllView();
    }

    public CourseView findOne(String code) {
        return courseRepository.findViewById(code);
    }

    public Set<String> enrollmentsOf(String code) {
        return courseRepository.findEnrollmentsById(code);
    }

}
