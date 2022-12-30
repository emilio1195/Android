package edu.uda.pets.POJO;

public class DataPostedPic {
    private String pic, likes;

    public DataPostedPic(String pic, String likes) {
        this.pic = pic;
        this.likes = likes;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

}
