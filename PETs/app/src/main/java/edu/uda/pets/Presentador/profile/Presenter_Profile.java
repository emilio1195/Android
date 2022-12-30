package edu.uda.pets.Presentador.profile;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.DataPostedPic;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.utils.FilesOperations;
import edu.uda.pets.view.profile.IF_View_Profile;

public class Presenter_Profile {
    IF_View_Profile if_view_profile;
    RecentPost dataPetSelected;
    Context context;
    public Presenter_Profile(Context context, IF_View_Profile if_view_profile, RecentPost dataPetSelected){
        this.if_view_profile = if_view_profile;
        this.dataPetSelected = dataPetSelected;
        this.context = context;
        setListProfilePics();
    }

    public JSONObject getJsonPet(String name_json_file) throws IOException {
        String jsonContent;
        JSONObject jsonObject = null;
        try {
            jsonContent = FilesOperations.readFile(context, "myProfile.json");
            jsonObject = new JSONObject(jsonContent);
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return  jsonObject;
    }

    private ArrayList<DataPostedPic> getListPicsMyProfile(){
        Gson gson = new Gson();
        String jsonContent;
        ArrayList<DataPostedPic> listPostedPics = new ArrayList<>();
        try{
            JSONObject jsonObject = getJsonPet("myProfile.json");
            String stringJsonPet = jsonObject.toString();

            JSONArray jsonArray = jsonObject.getJSONArray("postedPics");
            if_view_profile.informationPet(gson.fromJson(stringJsonPet, DataPet.class), jsonArray.length()+"", 0);

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonPosts = jsonArray.getJSONObject(i);
                String stringJsonPics = jsonPosts.toString();
                DataPostedPic dataPostedPic = gson.fromJson(stringJsonPics, DataPostedPic.class);
                listPostedPics.add(dataPostedPic);
            }
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }


        return  listPostedPics;
    }

    private ArrayList<DataPostedPic> getListPicsOtherProfile(){
        Gson gson = new Gson();
        String jsonContent;
        ArrayList<DataPostedPic> listPostedPics = new ArrayList<>();
        try{
            jsonContent = FilesOperations.readFile(context, "pets.json");
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray jsonArray = jsonObject.getJSONArray("pets");

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonPet = jsonArray.getJSONObject(i);
                String namePEt = jsonPet.getString("name");
                if (namePEt.equals(dataPetSelected.getName())){
                    String stringJsonPet = jsonPet.toString();
                    JSONArray jsonArrayPostedPics = jsonPet.getJSONArray("postedPics");
                    if_view_profile.informationPet(gson.fromJson(stringJsonPet, DataPet.class), jsonArrayPostedPics.length()+"", 1);
                    for (int j=0; j<jsonArrayPostedPics.length(); j++){
                        JSONObject jsonPosts = jsonArrayPostedPics.getJSONObject(j);
                        String stringJsonPics = jsonPosts.toString();
                        DataPostedPic dataPostedPic = gson.fromJson(stringJsonPics, DataPostedPic.class);
                        listPostedPics.add(dataPostedPic);
                    }
                    break;
                }
            }
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }


        return  listPostedPics;
    }

    private void setListProfilePics(){

        if (dataPetSelected != null){
            if_view_profile.startAdapter(if_view_profile.initAdapter(getListPicsOtherProfile()));
        }else{
            if_view_profile.startAdapter(if_view_profile.initAdapter(getListPicsMyProfile()));
        }
    }

}
