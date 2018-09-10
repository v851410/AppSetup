package com.appsetup.db.entity;

import com.appsetup.type.ContentType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "content")
public class Content {

    private Integer id;
    private ContentType type;
    private App app;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }
    @Enumerated(EnumType.STRING)
    @Column(length = 8, name = "type")
    public ContentType getType() {
        return type;
    }
    
    @ManyToOne
    @JoinColumn(name = "app_id", referencedColumnName = "id")
    @JsonBackReference
    public App getApp() {
        return this.app;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public void setApp(App app) {
        this.app = app;
    }
    
}
