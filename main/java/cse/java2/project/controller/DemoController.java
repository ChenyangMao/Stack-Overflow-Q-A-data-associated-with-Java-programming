package cse.java2.project.controller;

import cse.java2.project.model.Question;
import cse.java2.project.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class DemoController {

//    @Autowired
    private QuestionService questionService;

    public DemoController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping({"/"})
    public String showChart(Model model) {
        model.addAttribute("totalQuestions", questionService.getQuesCnt());
        model.addAttribute("unansweredQuestions", questionService.getQuesCntNoAns());
        model.addAttribute("avgValue", questionService.findAvg());
        model.addAttribute("maxValue", questionService.findMax());
        model.addAttribute("answerCounts", questionService.findAnsCnt());
        model.addAttribute("acceptedQuestions", questionService.getQuesCntAcAns());
        model.addAttribute("acceptTime", questionService.getAcTime());
        model.addAttribute("noAcceptedAnswer", questionService.getNoAc());
        model.addAttribute("wordcloudTag", questionService.getTags());
        model.addAttribute("wordcloudUpvote", questionService.getTagsUpvote());
        model.addAttribute("wordcloudView", questionService.getTagsView());
        model.addAttribute("ownerThread", questionService.cntOwnerThread());
        model.addAttribute("ownerAns", questionService.cutOwnerAns());
        model.addAttribute("ownerComm", questionService.cutOwnerComm());
        model.addAttribute("sortOwner", questionService.sortOwnerThread());
        model.addAttribute("getAPI", questionService.getAPI());
        return "chart"; // 返回HTML文件名，省略文件扩展名
    }

    @RequestMapping("getQuestionById")
    public String getQuestionById(Long id, Model model) {
        Question question = questionService.getQuestionByQuestionId(id);
        model.addAttribute("question", question);
        model.addAttribute("id", question.getId());
        model.addAttribute("date", question.getCreationDate());
        model.addAttribute("ans", question.getAnswer_count());
        model.addAttribute("view", question.getView_count());
        model.addAttribute("upvote", question.getUp_vote_count());
        return "question";
    }

    @RequestMapping({"getTagsInfo"})
    public String getTagsInfo(String content, Model model) {
        if (content.equals("related")) {
            model.addAttribute("content", questionService.getTags());
        } else if (content.equals("upvote")) {
            model.addAttribute("content", questionService.getTagsUpvote());
        } else if (content.equals("view")) {
            model.addAttribute("content", questionService.getTagsView());
        }
        return "tag";
    }

    @RequestMapping({"answer"})
    public String getAnswer(boolean isAccepted, Model model) {
        model.addAttribute("pageTitle",isAccepted);
        model.addAttribute("status", questionService.getAccepted(isAccepted));
        return "answer";
    }
}
