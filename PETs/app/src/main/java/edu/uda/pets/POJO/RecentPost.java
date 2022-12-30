package edu.uda.pets.POJO;

import java.io.Serializable;

public class RecentPost implements Serializable {
    private String name, breed, profilePic, recentPostPic, likes;
    private Boolean like;

    public RecentPost(String name, String breed, String profilePic, String recentPostPic, String likes, Boolean like) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
        this.recentPostPic = recentPostPic;
        this.likes = likes;
        this.like = like;
    }

    public RecentPost() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getRecentPostPic() {
        return recentPostPic;
    }

    public void setRecentPostPic(String recentPostPic) {
        this.recentPostPic = recentPostPic;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }
}
