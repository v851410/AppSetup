package com.appsetup.db.entity;

import com.appsetup.type.AppType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "app")
public class App {

    private int id;
    private String name;
    private AppType type;
    private User user;
    private Set<Content> content = new HashSet();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }  
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }


    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 8, name = "type")
    public AppType getType() {
        return type;
    }

    public void setType(AppType type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "app")
    @JsonManagedReference
    public Set<Content> getContent() {
        return content;
    }

    public void setContent(Set<Content> content) {
        this.content = content;
    }
}
