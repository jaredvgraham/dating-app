package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.rating.RatingDto;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.Rating;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.RatingRepository;
import com.evanw.datebyrate.repository.UserRepository;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatingService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public RatingService(UserRepository userRepository, RatingRepository ratingRepository){
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public void rateUser(RatingDto ratingDto){
        User user = getUserFromAuth();
        User ratedUser = getUserById(ratingDto.getUserId());
        Rating rating = new Rating();
        rating.setRatedUser(ratedUser);
        rating.setRaterUser(user);
        rating.setRate(ratingDto.getRating());
        updatedAverageRate(ratedUser, ratingDto.getRating());
        ratingRepository.save(rating);
        userRepository.save(ratedUser);
    }
    private void updatedAverageRate(User user, int rating){
        Map<String, Integer> attributes = getRatingAttributes(user.getId());
        int sum = attributes.get("sum") + rating + 5;
        int elementCount = attributes.get("length");
        if (elementCount == 0) {
            elementCount += 2;
        } else {
            elementCount += 1;
        }
        double averageRate = (double) sum / elementCount;
        int updatedAverageRate = (int) Math.ceil(averageRate);
        user.setAverageRate(updatedAverageRate);
        user.setRateCount(elementCount);
    }
    private User getUserFromAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + auth.getName()));
    }
    private Map<String, Integer> getRatingAttributes(Integer ratedUserId){
        Map<String, Integer> response = new LinkedHashMap<>();
        List<Integer> ratings = ratingRepository.findRatingsForUser(ratedUserId);
        Integer sum = ratings.stream().mapToInt(Integer::intValue).sum();
        Integer length = ratings.size();
        response.put("sum", sum);
        response.put("length", length);
        return response;
    }
    private User getUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with ID: " + id));
    }
}
