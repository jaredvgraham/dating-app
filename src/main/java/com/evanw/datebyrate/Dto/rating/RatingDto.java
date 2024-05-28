package com.evanw.datebyrate.Dto.rating;

public class RatingDto {

    private Integer userId;

    private int rating;

    public RatingDto(Integer userId, int rating){
        this.rating = rating;
        this.userId = userId;
    }
    public RatingDto(){}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
