package com.evanw.datebyrate.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "matches", indexes = {
        @Index(name = "idx_matches_key", columnList = "key"),
        @Index(name = "idx_matches_match_id", columnList = "match_id"),
        @Index(name = "idx_matches_user_id", columnList = "user_id"),
        @Index(name = "idx_matches_matched_user_id", columnList = "matched_user_id")
})
public class Match{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "key")
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_user_id")
    private User matchedUser;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    public Match(Integer matchId,
                 User user,
                 User matchedUser
    ) {
        this.matchId = matchId;
        this.user = user;
        this.matchedUser = matchedUser;
        this.matchDate = LocalDateTime.now();
    }
    public Match(){
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getMatchedUser() {
        return matchedUser;
    }

    public void setMatchedUser(User matchedUser) {
        this.matchedUser = matchedUser;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }
}
