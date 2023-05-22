package cse.java2.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Comment {
    @ManyToOne
    private Owner owner;

    @Id
    private Long comment_id;
    @ManyToMany
    private List<API> apis;

    public Comment(){}

    public Comment(Owner owner,long comment_id,List<API> apis){
        this.owner = owner;
        this.comment_id = comment_id;
        this.apis = apis;
    }
}
