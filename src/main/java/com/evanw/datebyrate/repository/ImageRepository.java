package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByProfileId(Integer profileId);
}
