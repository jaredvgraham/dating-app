package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferencesRepository extends JpaRepository<Preferences, Integer> {
    Preferences findByUserId(Integer id);


}
