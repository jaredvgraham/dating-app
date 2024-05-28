package com.evanw.datebyrate.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_info", indexes = {
        @Index(name = "idx_user_info_username", columnList = "username"),
        @Index(name = "idx_user_info_email", columnList = "email"),
        @Index(name = "idx_user_info_age", columnList = "age"),
        @Index(name = "idx_user_info_gender", columnList = "gender"),
        @Index(name = "idx_user_info_rate_count", columnList = "rate_count"),
        @Index(name = "idx_user_info_average_rate", columnList = "average_rate")
})
public class User extends BaseEntity implements UserDetails {

    // Parameters

    private String firstName;

    @NotEmpty
    @Column(unique = true)
    private String username;


    private String lastName;

    @Email
    @NotEmpty
    @Column(unique = true)
    private String email;

    @NotEmpty
    private String password;


    private int age;


    private String gender;


    private String sexualOrientation;


    private boolean emailVerified = true;

    private LocalDateTime lockOutTime;

    private Integer failedLoginAttempts = 0;

    private Integer rateCount = 0;

    private Integer averageRate = 0;

//    private String verificationToken;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "location", columnDefinition = "geometry(Point,4326)")
    private Point location;

    public Point getLocation() {
        return this.location;
    }

    @OneToOne
    @Nullable
    private RefreshToken refreshToken;

    // RELATIONSHIPS

    //PROFILE
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinColumn(name = "profile_id")
    private Profile profile;

    // LIKES
    @OneToMany(mappedBy = "liker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Like> likesGiven;

    @OneToMany(mappedBy = "liked", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Like> likesReceived;

    //RATING
    @OneToMany(mappedBy = "raterUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rating> ratingsGiven;

    @OneToMany(mappedBy = "ratedUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Rating> ratingsReceived;

    // MATCH
    @OneToMany(mappedBy = "user")
    private Set<Match> matches;

    // PREFERENCES
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
    fetch = FetchType.LAZY)
    private Preferences preferences;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateLocation(Point location) {
        this.location = location;
    }
    public void removeRefreshToken(){
        this.refreshToken = null;
    }
    // private location
}
