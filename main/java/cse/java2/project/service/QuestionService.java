package cse.java2.project.service;

import cse.java2.project.model.*;
import cse.java2.project.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final OwnerRepository ownerRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final APIRepository apiRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, TagRepository tagRepository, OwnerRepository ownerRepository, AnswerRepository answerRepository, CommentRepository commentRepository, APIRepository apiRepository) {
        this.tagRepository = tagRepository;
        this.ownerRepository = ownerRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.commentRepository = commentRepository;
        this.apiRepository = apiRepository;
    }

    //todo: Number of Answers
    public int getQuesCnt() {
        return questionRepository.Cnt();
    }

    public int getQuesCntNoAns() {
        return questionRepository.CntNoAns();
    }

    public int findMax() {
        return questionRepository.findMaxAnsCnt();
    }

    public int findAvg() {
        return questionRepository.findSumAnsCnt() / questionRepository.Cnt();
    }

    public Integer[] findAnsCnt() {
        return questionRepository.findAnsCnt();
    }

    //todo: Accepted Answers
    public int getQuesCntAcAns() {
        return questionRepository.cntAcAns();
    }

    public List<Long> getAcTime() {
        return questionRepository.getAcTime();
    }

    public int getNoAc() {
        return questionRepository.getCntNoAc();
    }

    //todo:Tags
    public List<Object[]> getTags() {
        return tagRepository.getTags();
    }

    public List<Object[]> getTagsUpvote() {
        return tagRepository.getTagsUpvote();
    }

    public List<Object[]> getTagsView() {
        return tagRepository.getTagsView();
    }

    //todo:Users
    public List<Integer> cntOwnerThread(){
        return ownerRepository.cntOwnerThread();
    }
    public List<Integer> cutOwnerAns(){
        return ownerRepository.cntOwnerAns();
    }
    public List<Integer> cutOwnerComm(){
        return ownerRepository.cutOwnerComm();
    }
    public List<Object[]> sortOwnerThread(){
        return ownerRepository.sortOwnerThread();
    }

    //todo:APIs
    public List<Object[]> getAPI(){
        return apiRepository.getAPI();
    }

    //todo:restAPI
    public Question getQuestionByQuestionId(long questionId) {
        return questionRepository.findById(questionId);
    }
    public List<Object[]> getAccepted(boolean accepted){
        return questionRepository.getAccepted(accepted);
    }

    @Transactional
    public void addQuestions() throws IOException {
        for (int q = 1; q < 11; q++) {
            String filePath = "C:\\Users\\cheny\\IdeaProjects\\2023-Spring-Java2-Project-Demo-master\\src\\main\\java\\data\\questions\\" + "question_" + q + ".json";
            String jsonContent = readJsonFromFile(filePath);
            JSONObject json = new JSONObject(jsonContent);
            JSONArray items = json.getJSONArray("items");

            for (int k = 0; k < items.length(); k++) {
                JSONObject item = items.getJSONObject(k);
                //todo: tags
                JSONArray jsonTags = item.getJSONArray("tags");
                List<Tag> tags = addTags(jsonTags);
                //todo: comments
                List<Comment> comments;
                if (item.has("comments")) {
                    JSONArray jsonComments = item.getJSONArray("comments");
                    comments = addComments(jsonComments);
                } else {
                    comments = new ArrayList<>();
                }
                //todo: owner
                JSONObject jsonOwner = item.getJSONObject("owner");
                Owner owner = addOwners(jsonOwner);
                //todo: answer_count
                int answer_count = item.getInt("answer_count");
                //todo: creation_date
                long creation_date = item.getLong("creation_date");
                //todo: question_id
                long question_id = item.getLong("question_id");
                //todo: body
//                String body = item.getString("body");
                List<API> apis = extractAPI(item.getString("body"));
                //todo: answers
                List<Answer> answers;
                if (item.has("answers")) {
                    JSONArray jsonAnswers = item.getJSONArray("answers");
                    answers = addAnswers(jsonAnswers);
                } else {
                    answers = new ArrayList<>();
                }

                int view_count = item.getInt("view_count");
                int up_vote_count = item.getInt("up_vote_count");

                questionRepository.save(new Question(tags, owner, answer_count, view_count, up_vote_count, creation_date, question_id, apis, answers, comments));
            }
        }
    }

    @Transactional
    public List<Answer> addAnswers(JSONArray jsonAnswers) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < jsonAnswers.length(); i++) {
            JSONObject item = jsonAnswers.getJSONObject(i);
            //todo: owner
            JSONObject jsonOwner = item.getJSONObject("owner");
            Owner owner = addOwners(jsonOwner);
            //todo: up_vote_count
            int up_vote_count = item.getInt("up_vote_count");
            //todo: isAccepted
            boolean is_accepted = item.getBoolean("is_accepted");
            //todo: creation_date
            long creation_date = item.getLong("creation_date");
//        //todo: answer_id
            long answer_id = item.getLong("answer_id");
            //todo: body
            List<API> apis = extractAPI(item.getString("body"));
            Answer answer = new Answer(owner, up_vote_count, is_accepted, creation_date, answer_id, apis);

            answerRepository.save(answer);
            answers.add(answer);
//            }
        }
        return answers;
    }

    @Transactional
    public List<Tag> addTags(JSONArray jsonTags) {
//        JSONArray jsonTags = item.getJSONArray("tags");
        List<Tag> tags = new ArrayList<>();
//            System.out.println(k+"'s tags:");
        for (int i = 0; i < jsonTags.length(); i++) {
            String tag = jsonTags.getString(i);
            Tag newTag = new Tag(tag);
            tagRepository.save(newTag);
            tags.add(newTag);
//                System.out.println(tag);
        }
        return tags;
    }

    @Transactional
    public Owner addOwners(JSONObject jsonOwner) {
//        JSONObject jsonOwner = item.getJSONObject("owner");
        Owner owner;
        String display_name = jsonOwner.getString("display_name");
        if (jsonOwner.has("user_id")) {
            long user_id = jsonOwner.getLong("user_id");
            owner = new Owner(user_id,display_name);
        } else {
            owner = new Owner(display_name);
        }
        ownerRepository.save(owner);
        return owner;
    }

    public List<Comment> addComments(JSONArray jsonComments) {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < jsonComments.length(); i++) {
            JSONObject jsonComment = jsonComments.getJSONObject(i);
            JSONObject jsonOwner = jsonComment.getJSONObject("owner");
            Owner owner = addOwners(jsonOwner);
            long comment_id = jsonComment.getLong("comment_id");
//            String body = jsonComment.getString("body");
            List<API> apis = extractAPI(jsonComment.getString("body"));

            Comment comment = new Comment(owner,comment_id, apis);
            commentRepository.save(comment);
            comments.add(comment);
        }
        return comments;
    }

    public void findID() {
        long[] id = questionRepository.findID();
        for (int i = 1; i < id.length + 1; i++) {
            System.out.print(id[i - 1]);
            if (i % 100 != 0) {
                System.out.print(";");
            } else {
                System.out.println("\"+");
            }
        }
    }

    private String readJsonFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();
        return content.toString();
    }

    private List<API> extractAPI(String body) {
        List<API> apis = new ArrayList<>();
        Document doc = Jsoup.parse(body);

        // 获取所有的<code>标签
        Elements codeElements = doc.select("code");

        // 提取Java类和方法调用
        String regex = "([a-zA-Z_]+\\.)+([a-zA-Z_]+)";
        Pattern pattern = Pattern.compile(regex);

        // 使用Set来存储已提取的值
        Set<String> extractedValues = new HashSet<>();

        for (Element codeElement : codeElements) {
            String code = codeElement.text();

            // 使用正则表达式匹配Java类和方法调用
            Matcher matcher = pattern.matcher(code);
            while (matcher.find()) {
                String m = matcher.group();
                if (extractedValues.add(m)) {
//                    System.out.println(match);
                    API m_api = new API(m);
                    apis.add(m_api);
                    apiRepository.save(m_api);
                    String[] matches = m.split("\\.");
                    for (String match : matches) {
                        if (extractedValues.add(match)&&match.length()>1) {
//                    System.out.println(match);
                            API api = new API(match);
                            apis.add(api);
                            apiRepository.save(api);
                        }
                    }

                }

            }
        }
        return apis;
    }
}