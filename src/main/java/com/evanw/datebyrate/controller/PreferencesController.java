package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.preferences.PreferencesDto;
import com.evanw.datebyrate.models.Preferences;
import com.evanw.datebyrate.repository.PreferencesRepository;
import com.evanw.datebyrate.service.PreferencesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PreferencesController {

    private final PreferencesService preferencesService;

    public PreferencesController(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @PostMapping("/preferences/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updatePreferences(@RequestBody PreferencesDto preferencesDto) {
        try {
            preferencesService.updatePreferences(preferencesDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Error updating preferences: " + e.getMessage());
        }
    }

    @GetMapping("/preferences")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PreferencesDto> getPreferences(){
        return preferencesService.getPreferences();
    }

//    @PostMapping("/preferences")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Preferences> updatePreferences(Preferences preferences){
//
//    }
}
