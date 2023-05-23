package cse.java2.project.controller;

import cse.java2.project.model.Answer;
import cse.java2.project.model.Question;
import cse.java2.project.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoRestController {
    private QuestionService questionService;

    public DemoRestController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionByQuestionId(id);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    @GetMapping("/tags/{content}")
    public ResponseEntity<List<Object[]>> getTagsInfo(@PathVariable String content) {
        List<Object[]> tagsInfo = new ArrayList<>();

        if (content.equals("related")) {
            tagsInfo = questionService.getTags();
        } else if (content.equals("upvote")) {
            tagsInfo = questionService.getTagsUpvote();
        } else if (content.equals("view")) {
            tagsInfo = questionService.getTagsView();
        }

        if (tagsInfo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tagsInfo);
    }


    @GetMapping("/answers/{isAccepted}")
    public ResponseEntity<List<Object[]>> getAnswer(@PathVariable boolean isAccepted) {
        List<Object[]> answers = questionService.getAccepted(isAccepted);
        if (answers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answers);
    }
}
