package cse.java2.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class API {

  @Id
  private String api_name;
//    @ManyToMany
//    private List<Question> questions;
//    @ManyToMany
//    private List<Answer> answers;
//    @ManyToMany
//    private List<Comment> comments;

  public API() {
  }

  public API(String api_name) {
    this.api_name = api_name;
  }
}
