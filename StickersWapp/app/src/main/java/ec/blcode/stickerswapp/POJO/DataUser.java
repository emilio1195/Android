package ec.blcode.stickerswapp.POJO;

import java.io.Serializable;
////////////se coloca serializable para poder empaquetarlo y enviarlo entre actividades
public class DataUser implements Serializable {
    private String Name_LastN, Gender, Birth_Date, email, number;
    private String photoUrl, Uuid, tokenFCM;

    public DataUser() {
    }

    public DataUser(String Uuid, String name_LastN, String gender, String birth_Date, String email, String number, String photoUrl, String tokenFCM) {
        Name_LastN = name_LastN;
        Gender = gender;
        Birth_Date = birth_Date;
        this.email = email;
        this.number = number;
        this.photoUrl = photoUrl;
        this.Uuid = Uuid;
        this.tokenFCM = tokenFCM;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName_LastN() {
        return Name_LastN;
    }

    public void setName_LastN(String name_LastN) {
        Name_LastN = name_LastN;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirth_Date() {
        return Birth_Date;
    }

    public void setBirth_Date(String birth_Date) {
        Birth_Date = birth_Date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
