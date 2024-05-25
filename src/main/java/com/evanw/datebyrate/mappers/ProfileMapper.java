package com.evanw.datebyrate.mappers;

import com.evanw.datebyrate.Dto.profile.ProfileDto;
import com.evanw.datebyrate.models.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileMapper {

    public ProfileDto toProfileDtoFromProfile(Profile profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getBiography(),
                profile.getHobbies(),
                profile.getSchoolOrWork(),
                profile.getMusicalArtists(),
                profile.getPronouns()
        );
    }
}
