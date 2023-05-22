package cse.java2.project.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Owner {

    @GeneratedValue
    private long owner_id;
    @Id
    private String display_name;

    public Owner(){

    }
    public Owner(long owner_id,String display_name){
        this.owner_id = owner_id;
        this.display_name = display_name;
    }

    public Owner(String display_name){
        this.display_name = display_name;
    }

}

