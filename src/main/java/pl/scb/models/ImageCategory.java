package pl.scb.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ImageCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    @Column
    private String name;

    @Column
    private long ownerId;

    public ImageCategory(long categoryId, String name, long ownerId){
        this.categoryId = categoryId;
        this.name = name;
        this.ownerId = ownerId;
    }

    public ImageCategory(String name, long ownerId){
        this.name = name;
        this.ownerId = ownerId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public long getOwnerId() {
        return ownerId;
    }
}
