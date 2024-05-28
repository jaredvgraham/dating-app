package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.RefreshToken;
import com.evanw.datebyrate.models.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


    Integer findIdByUsername(String username);
    Optional<User> findUserByRefreshToken(RefreshToken refreshToken);

    @Query(value = "SELECT * FROM user_info " +
            "WHERE EXTRACT(MONTH FROM dob) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(DAY FROM dob) = EXTRACT(DAY FROM CURRENT_DATE)",
            nativeQuery = true)
    List<User> findUsersWithBirthdayToday();

    Point findLocationByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM user_info ui WHERE ST_DWithin(" +
    "ui.location, ST_SetSRID(ST_MakePoint(:currentLongitude, :currentLatitude), 4326), :radius) " +
            "AND ui.id != :currentUserId " +
            "AND ui.gender IN (:genderPref) " +
            "AND ui.age >= :minAge AND ui.age <= :maxAge " +
            "AND ui.profile_id IS NOT NULL " +
            "AND ( " +
                "(SELECT rate_count FROM user_info WHERE id = :currentUserId) < 10 OR " +  // Current user has a rate count < 10, see everyone
                "ui.rate_count < 10 OR " +  // Include users with rate count < 10
                "ABS(ui.average_rate - (SELECT average_rate FROM user_info WHERE id = :currentUserId)) <= 1" +
            ") " +
            " AND NOT EXISTS (" +
                "SELECT 1 FROM viewed_profiles vp WHERE vp.viewer_id = :currentUserId AND vp.viewed_user_id = ui.id" +
            ")" +
            " AND NOT EXISTS (" +
                "SELECT 1 FROM like_info l WHERE l.liker_user_id = :currentUserId AND l.liked_user_id = ui.id" +
            ") " +
            " AND NOT EXISTS (" +
                "SELECT 1 FROM matches m WHERE (m.user_id = :currentUserId AND m.matched_user_id = ui.id) " +
                "OR (m.matched_user_id = :currentUserId AND m.user_id = ui.id)" +
            ") " +
            " LIMIT 10",
            nativeQuery = true)
    List<User> findNearbyUsers(@Param("currentLatitude") double currentLatitude,
                               @Param("currentLongitude") double currentLongitude,
                               @Param("radius") double radius,
                               @Param("currentUserId") Integer currentUserId,
                               @Param("genderPref") List<String> genderPref,
                               @Param("minAge") int minAge,
                               @Param("maxAge") int maxAge);

    @Query(value = "SELECT * FROM user_info ui WHERE ui.id != :currentUserId " +
    "AND ui.gender IN (:genderPref)" +
    " AND ui.age >= :minAge AND ui.age <= :maxAge" +
    " AND ui.profile_id IS NOT NULL" +
    " AND ( " +
        "(SELECT rate_count FROM user_info WHERE id = :currentUserId) < 10 OR " +
        "ui.rate_count < 10 OR " +
        "ABS(ui.average_rate - (SELECT average_rate FROM user_info WHERE id = :currentUserId)) <= 1" +
        ") " +
        " AND NOT EXISTS (" +
            "SELECT 1 FROM viewed_profiles vp WHERE vp.viewer_id = :currentUserId AND vp.viewed_user_id = ui.id" +
        ")" +
        " AND NOT EXISTS (" +
            "SELECT 1 FROM like_info l WHERE l.liker_user_id = :currentUserId AND l.liked_user_id = ui.id" +
        ") " +
        "AND NOT EXISTS (" +
            "SELECT 1 FROM matches m WHERE (m.user_id = :currentUserId AND m.matched_user_id = ui.id) " +
            "OR (m.matched_user_id = :currentUserId AND m.user_id = ui.id)" +
        ") " +
        " LIMIT 10",
        nativeQuery = true)
    List<User> findGlobalUsers(@Param("currentUserId") Integer currentUserId,
                               @Param("genderPref") List<String> genderPref,
                               @Param("minAge") int minAge,
                               @Param("maxAge") int maxAge);

//    Optional<User> findByVerificationToken(String token);

}
