package cse.java2.project.repository;

import cse.java2.project.model.API;
import cse.java2.project.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface APIRepository extends JpaRepository<API,Long> {
    @Query(value = "select apis_api_name, count(*) cnt from(\n" +
            "select apis_api_name\n" +
            "from answer_apis\n" +
            "union all\n" +
            "select apis_api_name\n" +
            "from question_apis\n" +
            "union all\n" +
            "select apis_api_name\n" +
            "from comment_apis\n" +
            ") subquery\n" +
            "group by apis_api_name order by cnt desc",nativeQuery = true)
    List<Object[]> getAPI();
}
