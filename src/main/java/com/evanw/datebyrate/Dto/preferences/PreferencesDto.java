package com.evanw.datebyrate.Dto.preferences;

import java.util.ArrayList;
import java.util.List;

public class PreferencesDto {

    private List<String> genderPref;

    private int minAge;

    private int maxAge;

    private int maxDist;

    private boolean global;

    public PreferencesDto(List<String> genderPref, int minAge, int maxAge, int maxDist, boolean global) {
        this.genderPref = genderPref;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxDist = maxDist;
        this.global = global;
    }

    public PreferencesDto() {
    }

    public List<String> getGenderPref() {
        return genderPref;
    }

    public void setGenderPref(List<String> genderPref) {
        this.genderPref = genderPref;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
