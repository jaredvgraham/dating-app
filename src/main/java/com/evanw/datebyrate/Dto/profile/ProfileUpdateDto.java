package com.evanw.datebyrate.Dto.profile;

public class ProfileUpdateDto {

    private String biography;

    private String hobbies;

    private String schoolOrWork;

    private String musicalArtists;

    private String pronouns;


    public ProfileUpdateDto(){}

    public ProfileUpdateDto(String biography, String hobbies, String schoolOrWork, String musicalArtists, String pronouns) {
        this.biography = biography;
        this.hobbies = hobbies;
        this.schoolOrWork = schoolOrWork;
        this.musicalArtists = musicalArtists;
        this.pronouns = pronouns;
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

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }
}
