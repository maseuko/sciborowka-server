package pl.scb.models;


import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PageOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ownerId;
    @Column
    private String name;

    public long getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }
}
