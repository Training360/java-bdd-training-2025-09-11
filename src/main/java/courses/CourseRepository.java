package courses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("""
        select new courses.CourseView(c.code, c.name) 
                from Course c order by c.code                
                """)
    List<CourseView> findAllView();

    @Query("""
        select new courses.CourseView(c.code, c.name)
            from Course c where c.code = :code
        """)
    CourseView findViewById(String code);

    @Query("""
        select distinct e
            from Course c
            join c.enrollments e
        where c.code = :code
        """)
    Set<String> findEnrollmentsById(String code);
}
