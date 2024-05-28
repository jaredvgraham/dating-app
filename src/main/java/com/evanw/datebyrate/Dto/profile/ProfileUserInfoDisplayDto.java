package com.evanw.datebyrate.Dto.profile;

import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ProfileUserInfoDisplayDto {
    private String firstName;
    private int age;
    private String biography;
    private String hobbies;
    private String schoolOrWork;
    private String musicalArtists;
//    private List<Image> images = new ArrayList<>();
    private Integer userId;
    private double distance;
    private String pronouns;

    private int rating;
    @JsonProperty("images")
    private List<Image> images;

    public ProfileUserInfoDisplayDto(String firstName,
                                     int age,
                                     String biography,
                                     String hobbies,
                                     String schoolOrWork,
                                     String musicalArtists,
                                     Integer userId,
                                     String pronouns,
                                     int rating) {
        this.firstName = firstName;
        this.age = age;
        this.biography = biography;
        this.hobbies = hobbies;
        this.schoolOrWork = schoolOrWork;
        this.musicalArtists = musicalArtists;
        this.images = new ArrayList<>();
        this.userId = userId;
        this.distance = 0;
        this.pronouns = pronouns;
        this.rating = rating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getSchoolOrWork() {
        return schoolOrWork;
    }

    public void setSchoolOrWork(String schoolOrWork) {
        this.schoolOrWork = schoolOrWork;
    }

    public String getMusicalArtists() {
        return musicalArtists;
    }

    public void setMusicalArtists(String musicalArtists) {
        this.musicalArtists = musicalArtists;
    }

//    public List<Image> getImages() {
//        return images;
//    }
//
//    public void setImages(List<Image> images) {
//        this.images = images;
//    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setImages(List<Image> images) {
        this.images = images;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getDistance(){
        return this.distance;
    }
    public void setPronouns(String pronouns){
        this.pronouns = pronouns;
    }
    public String getPronouns(){
        return this.pronouns;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
