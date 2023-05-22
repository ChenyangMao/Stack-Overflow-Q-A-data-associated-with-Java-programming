package cse.java2.project.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Answer {

  @ManyToOne
  private Owner owner;
  private int up_vote_count;
  private boolean is_accepted;
  private long creation_date;
  @Id
  private long answer_id;
  @ManyToMany
  private List<API> apis;

  public Answer() {

  }

  public Answer(Owner owner, int up_vote_count, boolean isAccepted, long creation_date,
      long answer_id, List<API> apis) {
    this.owner = owner;
    this.up_vote_count = up_vote_count;
    this.is_accepted = isAccepted;
    this.creation_date = creation_date;
    this.answer_id = answer_id;
    this.apis = apis;
  }


}
