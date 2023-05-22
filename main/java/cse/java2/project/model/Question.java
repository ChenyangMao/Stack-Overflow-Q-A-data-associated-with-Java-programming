package cse.java2.project.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class Question {
    @ManyToMany
    private List<Tag> tags;
    @ManyToOne
    private Owner owner;
    //private Answer accepted_answer;
    private int view_count;
    private int up_vote_count;
    private int answer_count;
    private long creation_date;
    @Id
    private long id;
    @ManyToMany
    private List<API> apis;
    @OneToMany
    private List<Answer> answers;
    @OneToMany
    private List<Comment> comments;

    public Question() {

    }

    public Question(List<Tag> tags,
                    Owner owner,
                    int answer_count,
                    int view_count,
                    int up_vote_count,
                    long creation_date,
                    long questionId,
                    List<API> apis,
                    List<Answer> answers,
                    List<Comment> comments
                    ){
        this.tags = tags;
        this.owner = owner;
        this.answer_count = answer_count;
        this.view_count = view_count;
        this.up_vote_count = up_vote_count;
        this.creation_date =creation_date;
        this.id =questionId;
        this.apis = apis;
        this.answers = answers;
        this.comments = comments;
    }
    public Question(long questionId) {
        this.id = questionId;
    }

    public int getAnswer_count(){
        return answer_count;
    }

    public long getId(){
        return id;
    }

    public Date getCreationDate(){
        return new Date(creation_date*1000);
    }

    public int getView_count(){
        return view_count;
    }

    public int getUp_vote_count(){
        return up_vote_count;
    }

}
