package com.evanw.datebyrate.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "idx_preferences_max_distance", columnList = "max_distance"),
        @Index(name = "idx_preferences_min_age", columnList = "min_age"),
        @Index(name = "idx_preferences_max_age", columnList = "max_age"),
        @Index(name = "idx_preferences_user_id", columnList = "user_id")
})
public class Preferences {
    @Id
    @GeneratedValue
    private Integer id;

    private int maxDistance;
    private int minAge;
    private int maxAge;
    @Nullable
    private boolean global;

    @ElementCollection
    private List<String> genderPref = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Preferences(Integer id, int maxDistance, int minAge, int maxAge, User user, ArrayList genderPref, boolean global) {
        this.id = id;
        this.maxDistance = maxDistance;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.user = user;
        this.genderPref = genderPref;
        this.global = false;
    }

    public Preferences() {
        this.maxAge = 35;
        this.minAge = 18;
        this.maxDistance = 50; // miles
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void addGenderPref(ArrayList genderPref){
        this.genderPref = genderPref;
    }
    public List<String> getGenderPref(){
        return this.genderPref;
    }
    public boolean isGlobal() {
        return global;
    }
    public void setGlobal(boolean global) {
        this.global = global;
    }
}
