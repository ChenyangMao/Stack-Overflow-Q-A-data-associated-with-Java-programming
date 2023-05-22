package cse.java2.project.repository;

import cse.java2.project.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findById(long id);

    @Query(value="select answer_id,qa.question_id from answer join question_answers qa on answer.answer_id = qa.answers_answer_id where is_accepted = ?",nativeQuery = true)
    List<Object[]> getAccepted(boolean accepted);
    @Query("SELECT count(*) FROM Question")
    Integer Cnt();

    @Query("SELECT count(*) FROM Question where answer_count = 0")
    Integer CntNoAns();

    @Query("SELECT MAX(answer_count) FROM Question")
    Integer findMaxAnsCnt();

    @Query("SELECT sum(answer_count) FROM Question")
    Integer findSumAnsCnt();

    @Query("SELECT answer_count FROM Question")
    Integer[] findAnsCnt();

    @Query("select id from Question where answer_count = 1")
    long[] findID();

    @Query(value = "select count(question_id) from question_answers join answer a on a.answer_id = question_answers.answers_answer_id where is_accepted = true",nativeQuery = true)
    int cntAcAns();

    @Query(value = "select a.creation_date - q.creation_date\n" +
            "from question_answers\n" +
            "         join question q on question_answers.question_id = q.id\n" +
            "         join answer a on a.answer_id = question_answers.answers_answer_id\n" +
            "where is_accepted = true",nativeQuery = true)
    List<Long> getAcTime();

    @Query(value = "select count(*)\n" +
            "from (select answer_id, a.up_vote_count,q.id\n" +
            "      from question_answers join answer a on question_answers.answers_answer_id = a.answer_id\n" +
            "      join question q on question_answers.question_id = q.id\n" +
            "      where a.is_accepted = true) ac\n" +
            "         join (select answer_id, a.up_vote_count,id\n" +
            "               from question_answers join answer a on question_answers.answers_answer_id = a.answer_id\n" +
            "               join question q2 on question_answers.question_id = q2.id\n" +
            "               where a.is_accepted = false) unac on ac.id = unac.id\n" +
            "where unac.up_vote_count > ac.up_vote_count", nativeQuery = true)
    Integer getCntNoAc();


}
