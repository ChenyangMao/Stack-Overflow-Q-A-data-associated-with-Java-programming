package cse.java2.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Tag {
    @Id
    private String tagName;
//    @ManyToMany
//    private List<Question> questions;

    public Tag() {

    }
    public Tag(String tagName){
        this.tagName = tagName;
    }


}
