package pl.scb.models;

import org.springframework.web.bind.annotation.RequestAttribute;

import javax.persistence.*;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String title;
    @Column
    private String description;

    public BlogPost(){}

    public BlogPost(@RequestAttribute("id") long id, @RequestAttribute("title") String title, @RequestAttribute("description") String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}