package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.ViewedProfiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewedProfilesRepository extends JpaRepository<ViewedProfiles, Integer> {
}
