package edu.uda.pets.POJO;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPet implements Serializable {
    private String name, breed, profilePic, description, followers, following;
    private ArrayList<DataPostedPic> postedPics;

    public DataPet(String name, String breed, String profilePic, String description, String followers, String following, ArrayList<DataPostedPic> postedPics) {
        this.name = name;
        this.breed = breed;
        this.profilePic = profilePic;
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.postedPics = postedPics;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public ArrayList<DataPostedPic> getPostedPics() {
        return postedPics;
    }

    public void setPostedPics(ArrayList<DataPostedPic> postedPics) {
        this.postedPics = postedPics;
    }
}
