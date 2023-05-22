package cse.java2.project.repository;

import cse.java2.project.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,String> {
//    Optional<Tag> findByTagName(String tagName);
    @Query(value = "select CONCAT('#', tags_tag_name, '#') AS tag_combination, count(*) cnt\n" +
            "from question_tags where tags_tag_name <> 'java'\n" +
            "group by tags_tag_name order by cnt desc ",nativeQuery = true)
    List<Object[]> getTags();

    @Query(value = "SELECT tag_combination, total_upvotes\n" +
            "FROM (\n" +
            "         SELECT CONCAT('#', qt.tags_tag_name, '#') AS tag_combination, SUM(q.up_vote_count) AS total_upvotes\n" +
            "         FROM question_tags qt\n" +
            "                  JOIN question q ON q.id = qt.question_id where tags_tag_name <> 'java'\n" +
            "         GROUP BY qt.tags_tag_name\n" +
            "\n" +
            "         UNION ALL\n" +
            "\n" +
            "         SELECT tag_combination, total_upvotes\n" +
            "         FROM (SELECT CONCAT('#', qt1.tags_tag_name, '##', qt2.tags_tag_name, '#') AS tag_combination,\n" +
            "             SUM(q.up_vote_count)                                         AS total_upvotes\n" +
            "             FROM question q\n" +
            "             JOIN question_tags qt1 ON q.id = qt1.question_id\n" +
            "             JOIN question_tags qt2 ON q.id = qt2.question_id\n" +
            "             WHERE qt1.tags_tag_name < qt2.tags_tag_name and qt1.tags_tag_name <> 'java' and qt2.tags_tag_name <> 'java'\n" +
            "             GROUP BY tag_combination) subquery\n" +
            "         ) subquery\n" +
            "\n" +
            "ORDER BY total_upvotes DESC",nativeQuery = true)
    List<Object[]> getTagsUpvote();

    @Query(value = "SELECT tag_combination, total_views\n" +
            "FROM (\n" +
            "         SELECT CONCAT('#', qt.tags_tag_name, '#') AS tag_combination, SUM(q.view_count) AS total_views\n" +
            "         FROM question_tags qt\n" +
            "                  JOIN question q ON q.id = qt.question_id where tags_tag_name <> 'java'\n" +
            "         GROUP BY qt.tags_tag_name\n" +
            "\n" +
            "         UNION ALL\n" +
            "\n" +
            "         SELECT tag_combination, total_views\n" +
            "         FROM (SELECT CONCAT('#', qt1.tags_tag_name, '##', qt2.tags_tag_name, '#') AS tag_combination,\n" +
            "                      SUM(q.view_count)                                         AS total_views\n" +
            "               FROM question q\n" +
            "                        JOIN question_tags qt1 ON q.id = qt1.question_id\n" +
            "                        JOIN question_tags qt2 ON q.id = qt2.question_id\n" +
            "               WHERE qt1.tags_tag_name < qt2.tags_tag_name and qt1.tags_tag_name <> 'java' and qt2.tags_tag_name <> 'java'\n" +
            "               GROUP BY tag_combination) subquery\n" +
            "     ) subquery\n" +
            "\n" +
            "ORDER BY total_views DESC",nativeQuery = true)
    List<Object[]> getTagsView();
}
