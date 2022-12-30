package edu.uda.pets.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uda.pets.Dialog.DialogPreviewImg;
import edu.uda.pets.POJO.DataPostedPic;
import edu.uda.pets.R;

public class Adapter_Profile_Posted_Pics extends RecyclerView.Adapter<Adapter_Profile_Posted_Pics.CvPicsProfileViewHolder> {

    private ArrayList<DataPostedPic> listfotosPerfil;
    private static int widthScreen = 0;
    private static int number_colms = 0;
    private static int size_pic = 0;

    @Override
    public CvPicsProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_foto_rating,parent,false);
        return new CvPicsProfileViewHolder(v);
    }

    public static class CvPicsProfileViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout containerCvPicProfile;
        ImageView pic_posted_profile;
        TextView rating;
        public CvPicsProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            containerCvPicProfile = (RelativeLayout) itemView.findViewById(R.id.CVPet);
            pic_posted_profile = (ImageView) itemView.findViewById(R.id.img_posted_profile);
            rating = (TextView) itemView.findViewById(R.id.txtrating);

            int number_spaces = number_colms + 1;
            int width_space =  itemView.getContext().getResources().getDimensionPixelSize(R.dimen.width_space);
            int width_total_spaces = number_spaces * width_space;
            int width_total_pics = widthScreen - width_total_spaces;
            size_pic = width_total_pics / number_colms;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CvPicsProfileViewHolder cvPicsProfileViewHolder, int position) {
        final DataPostedPic postedPics = listfotosPerfil.get(position);
        String imagePostedName = postedPics.getPic();
        int imageResourcePosted = activity.getResources().getIdentifier(imagePostedName, "drawable", activity.getPackageName());
        cvPicsProfileViewHolder.pic_posted_profile.setImageResource(imageResourcePosted);
        cvPicsProfileViewHolder.rating.setText(postedPics.getLikes());
        cvPicsProfileViewHolder.pic_posted_profile.setMinimumWidth(size_pic);
        cvPicsProfileViewHolder.pic_posted_profile.setMinimumHeight(size_pic);
        cvPicsProfileViewHolder.pic_posted_profile.setMaxWidth(size_pic);
        cvPicsProfileViewHolder.pic_posted_profile.setMaxHeight(size_pic);

        cvPicsProfileViewHolder.pic_posted_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogPreviewImg(activity, postedPics);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listfotosPerfil.size();
    }

    private final Activity activity;
    public Adapter_Profile_Posted_Pics(ArrayList<DataPostedPic> listfotosPerfil, Activity activity, int width_screen, int number_colms) {
        this.listfotosPerfil = listfotosPerfil;
        this.activity=activity;
        this.widthScreen = width_screen;
        this.number_colms = number_colms;
    }
}
