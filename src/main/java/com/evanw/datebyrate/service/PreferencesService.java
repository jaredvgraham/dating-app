package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.preferences.PreferencesDto;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.mappers.PreferencesMapper;
import com.evanw.datebyrate.models.Preferences;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.PreferencesRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferencesService {

    private final PreferencesRepository preferencesRepository;
    private final UserRepository userRepository;

    private final PreferencesMapper preferencesMapper;

    public PreferencesService(
            PreferencesRepository preferencesRepository,
            UserRepository userRepository,
            PreferencesMapper preferencesMapper
    ) {
        this.preferencesRepository = preferencesRepository;
        this.userRepository = userRepository;
        this.preferencesMapper = preferencesMapper;
    }

    @Transactional
    public void updatePreferences(PreferencesDto preferencesDto){
        User user = extractUserFromAuth();
        Preferences preferences = preferencesRepository.findByUserId(user.getId());

        updateScalarFields(preferences, preferencesDto);

        updateGenderPref(preferences, preferencesDto.getGenderPref());

//        preferencesRepository.save(preferences);
    }
    @Transactional
    public ResponseEntity<PreferencesDto> getPreferences(){
        Preferences preferences = extractUserFromAuth().getPreferences();
        return new ResponseEntity<>(preferencesMapper.toPreferencesDtoFromPreferences(preferences), HttpStatus.OK);
    }

    private void updateScalarFields(Preferences preferences, PreferencesDto preferencesDto){
        if (preferences.getMinAge() != preferencesDto.getMinAge()){
            preferences.setMinAge(preferencesDto.getMinAge());
        }
        if (preferences.getMaxAge() != preferencesDto.getMaxAge()){
            preferences.setMaxAge(preferencesDto.getMaxAge());
        }
        if (preferences.getMaxDistance() != preferencesDto.getMaxDist()){
            preferences.setMaxDistance(preferencesDto.getMaxDist());
        }
        if (preferences.isGlobal() != preferencesDto.isGlobal())
            preferences.setGlobal(preferencesDto.isGlobal());
    }
    private void updateGenderPref(Preferences preferences, List<String> newGenderPrefs){
        if (!preferences.getGenderPref().equals(newGenderPrefs)){
            preferences.getGenderPref().clear();;
            preferences.getGenderPref().addAll(newGenderPrefs);
        }
    }

    private User extractUserFromAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));
    }
}
