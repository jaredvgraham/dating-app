package com.evanw.datebyrate.service;

import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.models.ViewedProfiles;
import com.evanw.datebyrate.repository.UserRepository;
import com.evanw.datebyrate.repository.ViewedProfilesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ViewedProfileService {

    private ViewedProfilesRepository viewedProfilesRepository;
    private UserRepository userRepository;

    public ViewedProfileService(
            ViewedProfilesRepository viewedProfilesRepository,
            UserRepository userRepository
    ){
        this.viewedProfilesRepository = viewedProfilesRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void viewedProfile(User viewerId, Integer viewedId){
        Optional<User> viewed = userRepository.findById(viewedId);
        if (viewed.isEmpty()) {
            throw new UserNotFoundException("No user found");
        }
        ViewedProfiles viewedProfile = new ViewedProfiles();
        viewedProfile.setViewerId(viewerId);
        viewedProfile.setViewedUserId(viewedId);
        viewedProfile.setViewedAt(LocalDateTime.now());
        viewedProfilesRepository.save(viewedProfile);
    }
}
