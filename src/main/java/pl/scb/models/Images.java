package pl.scb.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String url;
    @Column
    private long postId;
    @Column
    private long categoryId;

    public Images(long id, String url, long post_id) {
        this.id = id;
        this.url = url;
        this.postId = post_id;
        this.categoryId = 1;
    }

    public Images(long id, String url, long post_id, long categoryId) {
        this.id = id;
        this.url = url;
        this.postId = post_id;
        this.categoryId = categoryId;
    }
    public String getUrl() {
        return url;
    }

    public long getPostId() {
        return postId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPostId(long post_id) {
        this.postId = post_id;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }
}