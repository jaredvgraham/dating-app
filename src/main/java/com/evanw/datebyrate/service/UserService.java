package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.profile.ProfileUserInfoDisplayDto;
import com.evanw.datebyrate.Dto.user.UserResponseDto;
import com.evanw.datebyrate.exception.NearbyUserException;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.mappers.UserMapper;
import com.evanw.datebyrate.models.Preferences;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.PreferencesRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.locationtech.jts.io.WKBReader.hexToBytes;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PreferencesRepository preferencesRepository;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PreferencesRepository preferencesRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.preferencesRepository = preferencesRepository;
    }

    public List<UserResponseDto> findAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDtoFromUser)
                .collect(Collectors.toList());
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public List<ProfileUserInfoDisplayDto> findNearbyUsers() throws ParseException {
        String username = getCurrentUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));
        Preferences preferences = preferencesRepository.findByUserId(currentUser.getId());

        List<User> nearbyUsers = fetchUsers(currentUser, preferences);
        return buildUserProfileDisplay(nearbyUsers, currentUser.getLocation());
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371;

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        return distance/1.609344;

    }
    private List<User> findGlobalUsers(User user, Preferences preferences){
        return userRepository.findGlobalUsers(
                user.getId(),
                preferences.getGenderPref(),
                preferences.getMinAge(),
                preferences.getMaxAge()
        );
    }
    private List<User> findLocalUsers(User user, Preferences preferences){
        double radiusDegrees = (preferences.getMaxDistance() * 1609.34) / 111000;
        return userRepository.findNearbyUsers(
                user.getLocation().getY(),
                user.getLocation().getX(),
                radiusDegrees,
                user.getId(),
                preferences.getGenderPref(),
                preferences.getMinAge(),
                preferences.getMaxAge()
        );
    }
    private String getCurrentUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    private List<User> fetchUsers(User currentUser, Preferences preferences) {
        if (preferences.isGlobal()) {
            return findGlobalUsers(currentUser, preferences);
        } else {
            List<User> localUsers = findLocalUsers(currentUser, preferences);
            return localUsers.isEmpty() ? findGlobalUsers(currentUser, preferences) : localUsers;
        }
    }
    private List<ProfileUserInfoDisplayDto> buildUserProfileDisplay(List<User> nearbyUsers, Point currentUserLocation) {
        List<ProfileUserInfoDisplayDto> profileDisplay = new ArrayList<>();
        double currentLatitude = currentUserLocation.getY();
        double currentLongitude = currentUserLocation.getX();

        for (User user : nearbyUsers) {
            profileDisplay.add(createProfileDisplayDto(user, currentLatitude, currentLongitude));
        }

        if (profileDisplay.isEmpty()) {
            throw new NearbyUserException("No nearby users");
        }

        return profileDisplay;
    }
    private ProfileUserInfoDisplayDto createProfileDisplayDto(User user, double currentLatitude, double currentLongitude) {
        double userLatitude = user.getLocation().getY();
        double userLongitude = user.getLocation().getX();
        double distance = calculateDistance(currentLatitude, currentLongitude, userLatitude, userLongitude);
        double roundedDistance = Math.round(distance * 10.0) / 10.0;

        ProfileUserInfoDisplayDto dto = new ProfileUserInfoDisplayDto(
                user.getFirstName(),
                user.getAge(),
                user.getProfile().getBiography(),
                user.getProfile().getHobbies(),
                user.getProfile().getSchoolOrWork(),
                user.getProfile().getMusicalArtists(),
                user.getId(),
                user.getProfile().getPronouns(),
                user.getAverageRate()
        );
        dto.setImages(user.getProfile().getImages());
        dto.setDistance(Math.max(1, (int) roundedDistance));
        return dto;
    }
}
//        // Retrieve the current users username
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        // Find the current user and their preferences
//        Optional<User> currentUser = userRepository.findByUsername(username);
//
//        if (currentUser.isEmpty()) {
//            throw new UserNotFoundException("No user found with username " + username);
//        }
//        User user = currentUser.get();
//        Preferences preferences = preferencesRepository.findByUserId(currentUser.get().getId());
//        List<User> nearbyUsers;
//        // Convert the user's preferred radius to meters
//        double currentLatitude = currentUser.get().getLocation().getY();
//        double currentLongitude = currentUser.get().getLocation().getX();
//        if (preferences.isGlobal()) {
//            System.out.println("In is global: " + preferences.isGlobal());
//            nearbyUsers = findGlobalUsers(currentUser.get(), preferences);
//        }
//        else {
//            nearbyUsers = findLocalUsers(currentUser.get(), preferences);
//            if (nearbyUsers.isEmpty()){
//                nearbyUsers = findGlobalUsers(currentUser.get(), preferences);
//            }
//            }
//        List<ProfileUserInfoDisplayDto> profileDisplay = new ArrayList<>();
//        for (User nearbyUser : nearbyUsers) {
//            double nearbyLat = nearbyUser.getLocation().getY();
//            double nearbyLon = nearbyUser.getLocation().getX();
//            System.out.println(nearbyLat + " " + nearbyLon + " " + currentLatitude + " " + currentLongitude);
//            double distance = calculateDistance(currentLatitude, currentLongitude, nearbyLat, nearbyLon);
//            String formattedDistance = String.format("%.1f", distance);
//            double roundedDistance = Double.parseDouble(formattedDistance);
//            System.out.println(distance);
//            ProfileUserInfoDisplayDto profileUserInfoDisplayDto = new ProfileUserInfoDisplayDto(
//                    nearbyUser.getFirstName(),
//                    nearbyUser.getAge(),
//                    nearbyUser.getProfile().getBiography(),
//                    nearbyUser.getProfile().getHobbies(),
//                    nearbyUser.getProfile().getSchoolOrWork(),
//                    nearbyUser.getProfile().getMusicalArtists(),
//                    nearbyUser.getProfile().getUserObj().getId(),
//                    nearbyUser.getProfile().getPronouns(),
//                    nearbyUser.getAverageRate()
//            );
//
//            profileUserInfoDisplayDto.setImages(nearbyUser.getProfile().getImages());
//            if ((int)distance < 1) {
//                profileUserInfoDisplayDto.setDistance(1);
//            }
//            profileUserInfoDisplayDto.setDistance(roundedDistance);
//            profileDisplay.add(profileUserInfoDisplayDto);
//        }
//        if (profileDisplay.isEmpty()) {
//            throw new NearbyUserException("No nearby users.");
//        }
//        return profileDisplay;

