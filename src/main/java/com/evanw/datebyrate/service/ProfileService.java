package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.ExistsDto;
import com.evanw.datebyrate.Dto.profile.ProfileDto;
import com.evanw.datebyrate.Dto.profile.ProfileResponseDto;
import com.evanw.datebyrate.Dto.profile.ProfileUpdateDto;
import com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.mappers.ProfileMapper;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Preferences;
import com.evanw.datebyrate.models.Profile;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.ImageRepository;
import com.evanw.datebyrate.repository.ProfileRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private int nextProfileIndex = 0;
    private static final int MAX_FREE_SWIPES = 10;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ProfileMapper profileMapper;

    @Value("${upload.dir}")
    private String uploadDir;

    public ProfileService(
            ProfileRepository profileRepository,
            ProfileMapper profileMapper,
            UserRepository userRepository,
            ImageRepository imageRepository
    ) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Profile createProfile(Integer userId, ProfileDto profileDto) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        User user = getUserById(userId);
        // Fetch the user from the database using provides uerId
        if (user.getProfile() != null) {
            throw new IllegalStateException("User already has a profile");
        }
        Profile profile = new Profile();
        profile.setFirstName(user.getFirstName());
        profile.setAge(user.getAge());
        profile.setUserObj(user);
        profile.setBiography(profileDto.getBiography());
        profile.setHobbies(profileDto.getHobbies());
        profile.setSchoolOrWork(profileDto.getSchoolOrWork());
        profile.setMusicalArtists(profileDto.getMusicalArtists());
        profile.setUsername(user.getUsername());
        profile.setPronouns(profileDto.getPronouns());

        Profile savedProfile = profileRepository.save(profile);
        user.setProfile(savedProfile);
        userRepository.save(user);

        return profile;
    }
    @Transactional
    public List<Profile> findAllProfiles() {
        return profileRepository.findAll();
    }
    @Transactional
    public ResponseEntity<ProfileUserInfoDisplayDto> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        ProfileUserInfoDisplayDto profile = profileRepository.findUserProfileDisplay(currentUsername);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<Profile> profileInst = profileRepository.findByUserObj_Id(profile.getUserId());
        if (profileInst.isEmpty()) {
            throw new RuntimeException("No profile found");
        }
        List<Image> images = imageRepository.findAllByProfileId(profileInst.get().getId());
        profile.setImages(images);
        return ResponseEntity.ok(profile);
    }
    @Transactional
    public Profile getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getProfile();
    }
    @Transactional
    public boolean doesProfileExist() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean exists = profileRepository.existsByUsername(username);
        System.out.println(exists);
        return exists;
    }
    @Transactional
    public ResponseEntity<?> updateProfile(ProfileUpdateDto profileDto, MultipartFile[] newImages){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));
        Profile profile = user.getProfile();

        updateProfileFields(profile, profileDto);
        if (newImages != null) {
            updateProfileImages(profile, newImages);
        }


        profileRepository.save(profile);

        return ResponseEntity.ok(HttpStatus.OK);

    }

    private User getUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found"));
    }

//    private void updateProfileFields(Profile profile, ProfileDto profileDto) {
//        if (profile.getBiography() != null && !profile.getBiography().equals(profileDto.getBiography())) {
//            profile.setBiography(profileDto.getBiography());
//        }
//        if (profile.getHobbies() != null && !profile.getHobbies().equals(profileDto.getHobbies())) {
//            profile.setHobbies(profileDto.getHobbies());
//        }
//        if (profile.getHobbies() != null && !profile.getSchoolOrWork().equals(profileDto.getSchoolOrWork())) {
//            profile.setSchoolOrWork(profileDto.getSchoolOrWork());
//        }
//        if (profile.getMusicalArtists() != null && !profile.getMusicalArtists().equals(profileDto.getMusicalArtists())) {
//            profile.setMusicalArtists(profileDto.getMusicalArtists());
//        }
//        if (profile.getPronouns() != null && !profile.getPronouns().equals(profileDto.getPronouns())) {
//            profile.setPronouns(profileDto.getPronouns());
//        }
//    }
    private void updateProfileFields(Profile profile, ProfileUpdateDto profileDto) {
        if (profile.getBiography() != null && !profile.getBiography().equals(profileDto.getBiography())) {
            profile.setBiography(profileDto.getBiography());
        } else if (profile.getBiography() == null && profileDto.getBiography() != null) {
            profile.setBiography(profileDto.getBiography());
        }

        if (profile.getHobbies() != null && !profile.getHobbies().equals(profileDto.getHobbies())) {
            profile.setHobbies(profileDto.getHobbies());
        } else if (profile.getHobbies() == null && profileDto.getHobbies() != null) {
            profile.setHobbies(profileDto.getHobbies());
        }

        if (profile.getSchoolOrWork() != null && !profile.getSchoolOrWork().equals(profileDto.getSchoolOrWork())) {
            profile.setSchoolOrWork(profileDto.getSchoolOrWork());
        } else if (profile.getSchoolOrWork() == null && profileDto.getSchoolOrWork() != null) {
            profile.setSchoolOrWork(profileDto.getSchoolOrWork());
        }

        if (profile.getMusicalArtists() != null && !profile.getMusicalArtists().equals(profileDto.getMusicalArtists())) {
            profile.setMusicalArtists(profileDto.getMusicalArtists());
        } else if (profile.getMusicalArtists() == null && profileDto.getMusicalArtists() != null) {
            profile.setMusicalArtists(profileDto.getMusicalArtists());
        }

        if (profile.getPronouns() != null && !profile.getPronouns().equals(profileDto.getPronouns())) {
            profile.setPronouns(profileDto.getPronouns());
        } else if (profile.getPronouns() == null && profileDto.getPronouns() != null) {
            profile.setPronouns(profileDto.getPronouns());
        }
    }

    private void updateProfileImages(Profile profile, MultipartFile[] newImages) {
        if (newImages != null && newImages.length > 0) {
            List<Image> existingImages = profile.getImages();
            for (Image image : existingImages) {
                imageRepository.delete(image);
            }
        }
        profile.getImages().clear();

        for (MultipartFile file : newImages) {
            if (!file.isEmpty()) {
                try {
                    String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path savePath = Paths.get(uploadDir).resolve(filename);
                    if (!Files.exists(savePath)) {
                        System.out.println("Trying to make a new directory");
                    }
                    Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/images/")
                            .path(filename)
                            .toUriString();

                    Image image = new Image();
                    image.setImageUrl(imageUrl);
                    image.setProfile(profile);

                    imageRepository.save(image);
                    profile.getImages().add(image);
                } catch (IOException e) {
                    throw new RuntimeException("Error processing image in image update", e);
                }
            }
        }
    }
//    @Transactional
//    public ResponseEntity<Profile> getNextProfile() {
//        if (nextProfileIndex < profilesList.size() && nextProfileIndex <= MAX_FREE_SWIPES) {
//            Profile nextProfile = profileList.get(nextProfileIndex);
//            nextProfileIndex++;
//            return ResponseEntity.ok(nextProfile);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}

