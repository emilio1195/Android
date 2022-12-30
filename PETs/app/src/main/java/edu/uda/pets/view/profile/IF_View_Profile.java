package edu.uda.pets.view.profile;

import java.util.ArrayList;

import edu.uda.pets.Adapter.Adapter_Profile_Posted_Pics;
import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.DataPostedPic;

public interface IF_View_Profile {
    void initRecyclerView();
    Adapter_Profile_Posted_Pics initAdapter (ArrayList<DataPostedPic> list_profile_pics);
    void startAdapter(Adapter_Profile_Posted_Pics adapter_profilePostedPics);
    void informationPet(DataPet dataPet, String posts, int type_profile);
}
