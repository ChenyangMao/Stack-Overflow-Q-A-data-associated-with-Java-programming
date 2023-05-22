package cse.java2.project.repository;

import cse.java2.project.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query(value = "select count(*) cnt\n" +
            "from (select owner_display_name, qa.question_id\n" +
            "      from owner\n" +
            "               join answer a on owner.display_name = a.owner_display_name\n" +
            "               join question_answers qa on a.answer_id = qa.answers_answer_id\n" +
            "      group by owner_display_name, qa.question_id\n" +
            "      union\n" +
            "      select owner_display_name, qc.question_id\n" +
            "      from owner\n" +
            "               join comment c on owner.display_name = c.owner_display_name\n" +
            "               join question_comments qc on c.comment_id = qc.comments_comment_id\n" +
            "      group by owner_display_name, question_id\n" +
            "      union\n" +
            "      select owner_display_name, id\n" +
            "      from owner\n" +
            "               join question q on owner.display_name = q.owner_display_name\n" +
            "      group by owner_display_name, id) subquery\n" +
            "group by owner_display_name order by cnt desc ;",nativeQuery = true)
    List<Integer> cntOwnerThread();

    @Query(value = "select count(*) from(\n" +
            "select owner_display_name, qa.question_id\n" +
            "from owner\n" +
            "         join answer a on owner.display_name = a.owner_display_name\n" +
            "         join question_answers qa on a.answer_id = qa.answers_answer_id\n" +
            "group by owner_display_name, qa.question_id) x\n" +
            "group by owner_display_name",nativeQuery = true)
    List<Integer> cntOwnerAns();

    @Query(value = "select count(*)\n" +
            "from (select owner_display_name, qc.question_id\n" +
            "      from owner\n" +
            "               join comment c on owner.display_name = c.owner_display_name\n" +
            "      join question_comments qc on c.comment_id = qc.comments_comment_id\n" +
            "      group by owner_display_name, qc.question_id) x\n" +
            "group by owner_display_name",nativeQuery = true)
    List<Integer> cutOwnerComm();

    @Query(value = "select DENSE_RANK() OVER (ORDER BY cnt DESC) AS rank,owner_display_name, cnt from(\n" +
            "select owner_display_name,count(*) cnt\n" +
            "from (select owner_display_name, qa.question_id\n" +
            "      from owner\n" +
            "               join answer a on owner.display_name = a.owner_display_name\n" +
            "               join question_answers qa on a.answer_id = qa.answers_answer_id\n" +
            "      group by owner_display_name, qa.question_id\n" +
            "      union\n" +
            "      select owner_display_name, qc.question_id\n" +
            "      from owner\n" +
            "               join comment c on owner.display_name = c.owner_display_name\n" +
            "               join question_comments qc on c.comment_id = qc.comments_comment_id\n" +
            "      group by owner_display_name, question_id\n" +
            "      union\n" +
            "      select owner_display_name, id cntQues\n" +
            "      from owner\n" +
            "               join question q on owner.display_name = q.owner_display_name\n" +
            "      group by owner_display_name, id) subquery\n" +
            "group by owner_display_name\n" +
            "order by cnt desc limit 30) result order by cnt",nativeQuery = true)
    List<Object[]> sortOwnerThread();
}
