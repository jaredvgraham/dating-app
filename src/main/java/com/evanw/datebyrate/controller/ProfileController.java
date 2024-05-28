package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.ExistsDto;
import com.evanw.datebyrate.Dto.profile.ProfileDto;
import com.evanw.datebyrate.Dto.profile.ProfileResponseDto;
import com.evanw.datebyrate.Dto.profile.ProfileUpdateDto;
import com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto;
import com.evanw.datebyrate.exception.ImageException;
import com.evanw.datebyrate.mappers.ProfileMapper;
import com.evanw.datebyrate.models.Image;
import com.evanw.datebyrate.models.Profile;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.ImageRepository;
import com.evanw.datebyrate.repository.ProfileRepository;
import com.evanw.datebyrate.service.ProfileService;
import com.evanw.datebyrate.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@RestController
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    @Value("${upload.dir}")
    private String uploadDir;


    public ProfileController(ProfileService profileService,
                             UserService userService,
                             ImageRepository imageRepository,
                             ProfileMapper profileMapper,
                             ProfileRepository profileRepository
    ){
        this.profileService = profileService;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
    }
    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public String createProfile(@ModelAttribute ProfileDto profileDto, @RequestParam("images") MultipartFile[] images) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Profile profile = profileService.createProfile(user.getId(), profileDto);

        handleImageUpload(images, profile);
//        ProfileResponseDto response = new ProfileResponseDto(
//                profile.getUserObj().getId(),
//                profile.getBiography(),
//                profile.getHobbies(),
//                profile.getSchoolOrWork(),
//                profile.getAge(),
//                profile.getImages()
//        );
        return "Profile successfully created.";
    }

    private void handleImageUpload(MultipartFile[] images, Profile profile) {
        if (images != null && images.length > 0) {
            if (images.length > 7) {
                throw new ImageException("Too many images");
            }

            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    if (file.getSize() > 1048576) {
                        throw new ImageException("File too large");
                    }
                    try {
                        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        Path savePath = Paths.get(uploadDir).resolve(filename);
                        if (!Files.exists(savePath)) {
//                            Files.createDirectories(savePath.getParent());
                            System.out.println("trying to make a new directory");
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
                        throw new RuntimeException("Error processing image", e);
                    }
                }
            }
        }
    }
//    @GetMapping("/users/profile")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Profile> findAllProfiles() {
//        return profileService.findAllProfiles();
//    }
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfileUserInfoDisplayDto> getUserProfile() {
        return profileService.getUserProfile();
    }
    @GetMapping("/profile-exist")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExistsDto> doesProfileExist() {
        boolean exists = profileService.doesProfileExist();
        ExistsDto exist = new ExistsDto();
        exist.setExists(exists ? "yes" : "no");
        return ResponseEntity.ok(exist);
    }

//    @PostMapping("/profile-update")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<?> updateProfile(@ModelAttribute ProfileUpdateDto profileDto,
//                                           @RequestParam("images") MultipartFile[] newImages) {
//        System.out.println(profileDto.getPronouns() + profileDto.getBiography() + profileDto.getHobbies());
//        return profileService.updateProfile(profileDto, newImages);
//    }
    @PostMapping("/profile-update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateProfile(
            @RequestParam("biography") String biography,
            @RequestParam("hobbies") String hobbies,
            @RequestParam("schoolOrWork") String schoolOrWork,
            @RequestParam("musicalArtists") String musicalArtists,
            @RequestParam("pronouns") String pronouns,
            @RequestPart(value = "images", required = false) MultipartFile[] newImages) {

        ProfileUpdateDto profileDto = new ProfileUpdateDto(biography, hobbies, schoolOrWork, musicalArtists, pronouns);

        System.out.println(profileDto.getPronouns() + profileDto.getBiography() + profileDto.getHobbies());
        return profileService.updateProfile(profileDto, newImages);
    }
//    @GetMapping("/next-profile")
//    public ResponseEntity<Profile> getNextProfile(){
//        return profileService.getNextProfile();
//    }
}
