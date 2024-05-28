package com.evanw.datebyrate.mappers;

import com.evanw.datebyrate.Dto.preferences.PreferencesDto;
import com.evanw.datebyrate.models.Preferences;
import org.springframework.stereotype.Service;

@Service
public class PreferencesMapper {

    public PreferencesDto toPreferencesDtoFromPreferences(Preferences preferences){
        return new PreferencesDto(
                preferences.getGenderPref(),
                preferences.getMinAge(),
                preferences.getMaxAge(),
                preferences.getMaxDistance(),
                preferences.isGlobal()
        );
    }
}
