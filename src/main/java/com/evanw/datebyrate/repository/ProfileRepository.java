package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUserObj_Id(Integer userId);
    boolean existsByUsername(String username);

    @Query("SELECT new com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto(" +
    "u.firstName, u.age, p.biography, p.hobbies, p.schoolOrWork, p.musicalArtists, u.id, p.pronouns, u.averageRate) " + "" +
            "FROM User u JOIN u.profile p WHERE u.username = :username")
    ProfileUserInfoDisplayDto findUserProfileDisplay(String username);

}
