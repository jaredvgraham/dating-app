package com.evanw.datebyrate.Dto.profile;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfileDto {

//    private Integer userId;

    private String biography;
    private String hobbies;
    private String schoolOrWork;
    private String musicalArtists;
    private String pronouns;

    @Nullable
    private List<MultipartFile> images = new ArrayList<>();

    public ProfileDto() {
    }

    public ProfileDto(Integer userId, String biography, String hobbies, String schoolOrWork, String musicalArtists, String pronouns) {
//        this.userId = userId;
        this.biography = biography;
        this.hobbies = hobbies;
        this.schoolOrWork = schoolOrWork;
        this.musicalArtists = musicalArtists;
        this.pronouns = pronouns;
    }

}
