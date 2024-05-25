package com.evanw.datebyrate.service;

import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.ProfileRepository;
import com.evanw.datebyrate.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class UserAgeUpdateService {
    private  final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserAgeUpdateService(UserRepository userRepository,
                                ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateUsersAge() {
        List<User> usersWithBirthdayToday = userRepository.findUsersWithBirthdayToday();
        for (User user : usersWithBirthdayToday) {
            int age = calculateAge(user.getDob());
            user.setAge(age);
            if (user.getProfile() != null) {
                user.getProfile().setAge(age);
            }
            userRepository.save(user);
            profileRepository.save(user.getProfile());
        }

    }
    private int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }
}
