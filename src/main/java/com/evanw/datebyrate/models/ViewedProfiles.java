package com.evanw.datebyrate.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ViewedProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    private User viewerId;

    @Column(name = "viewed_user_id", nullable = false)
    private Integer viewedUserId;

    private LocalDateTime viewedAt;


    public ViewedProfiles(
            Integer id,
            User viewerId,
            Integer viewedUserId
    ){
        this.id = id;
        this.viewerId = viewerId;
        this.viewedUserId = viewedUserId;
        this.viewedAt = LocalDateTime.now();
    }

    public ViewedProfiles() {

    }

    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public User getViewerId(){
        return this.viewerId;
    }
    public void setViewerId(User viewerId){
        this.viewerId = viewerId;
    }
    public Integer getViewedUserId(){
        return this.viewedUserId;
    }
    public void setViewedUserId(Integer viewedUserId){
        this.viewedUserId = viewedUserId;
    }
    public LocalDateTime getViewedAt(){
        return this.viewedAt;
    }
    public void setViewedAt(LocalDateTime viewedAt){
        this.viewedAt = LocalDateTime.now();
    }
}
