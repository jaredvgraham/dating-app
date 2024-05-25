package com.evanw.datebyrate.Dto.profile;

import com.evanw.datebyrate.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ProfileResponseDto {

    private Integer userId;
    private String biography;
    private String hobbies;
    private String schoolOrWork;
    private int age;
    private List<Image> images = new ArrayList<>();

    public ProfileResponseDto(Integer userId, String biography, String hobbies, String schoolOrWork, int age, List<Image> images) {
        this.userId = userId;
        this.biography = biography;
        this.hobbies = hobbies;
        this.schoolOrWork = schoolOrWork;
        this.age = age;
        this.images = images;
    }

    public ProfileResponseDto() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
