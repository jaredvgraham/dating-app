package com.evanw.datebyrate.Dto.user;

public class UserResponseDto {

    private Integer id;

    private String firstname;
    private int age;
    private String gender;
    private String sexualOrientation;
    private String message;

    public UserResponseDto(String message) {
        this.message = message;
    }
    public UserResponseDto(Integer id,
                           String firstname,
                           int age,
                           String gender,
                           String sexualOrientation
    ){
        this.id = id;
        this.firstname = firstname;
        this.age = age;
        this.gender = gender;
        this.sexualOrientation = sexualOrientation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSexualOrientation() {
        return sexualOrientation;
    }

    public void setSexualOrientation(String sexualOrientation) {
        this.sexualOrientation = sexualOrientation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
