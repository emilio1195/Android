package edu.uda.pets.view.profile;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import edu.uda.pets.Adapter.Adapter_Profile_Posted_Pics;
import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.DataPostedPic;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.Presentador.profile.Presenter_Profile;
import edu.uda.pets.R;
import edu.uda.pets.WindowPets;


/**
 * https://github.com/lopspower/CircularImageView
 */
public class View_Profile_Fragment extends Fragment implements IF_View_Profile {
    private RecyclerView recyclerView;
    private TextView txt_name_breed_Pet, txt_followers, txt_following, txt_posts, txt_description_edit, txt_description_view;
    private CircularImageView img_profile;
    private LinearLayout container_main, container_controlls_Pview, container_controlls_Pedit;
    private Adapter_Profile_Posted_Pics adapter_profilePostedPics;
    private Presenter_Profile presenter_profile;
    private int number_colms = 3;
    private int width_screen = 0;
    private String key_controlls_Pview = null;
    private String key_View_profile;
    private RecentPost dataPetSelected;
    private BadgeDrawable badgeMessages;

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        img_profile = (CircularImageView) v.findViewById(R.id.img_profile);
        txt_name_breed_Pet = (TextView) v.findViewById(R.id.txt_name_breed_Pet);
        txt_followers = (TextView) v.findViewById(R.id.txt_followers);
        txt_following = (TextView) v.findViewById(R.id.txt_following);
        txt_posts = (TextView) v.findViewById(R.id.txt_posts);
        txt_description_edit = (TextView) v.findViewById(R.id.txt_description_edit);
        txt_description_view = (TextView) v.findViewById(R.id.txt_description_view);
        container_main = (LinearLayout) v.findViewById(R.id.container);
        container_controlls_Pview = (LinearLayout) v.findViewById(R.id.container_controlls_Pview);
        container_controlls_Pedit = (LinearLayout) v.findViewById(R.id.container_controlls_Pedit);
        recyclerView = (RecyclerView) v.findViewById(R.id.RVperfil);

        key_View_profile = v.getResources().getString(R.string.Key_View_FProfile);
        key_controlls_Pview = "";
        dataPetSelected = null;

        if(getArguments() != null){
            key_controlls_Pview = getArguments().getString(getString(R.string.Key_Data_FProfile));
            Bundle bundlePetSelected = getArguments().getBundle("bundlePetSelected");
            if (bundlePetSelected != null)
                dataPetSelected = (RecentPost) bundlePetSelected.getSerializable("petSelected");

        }

        if (key_controlls_Pview.equals(key_View_profile)){
            container_controlls_Pview.setVisibility(View.VISIBLE);
            container_controlls_Pedit.setVisibility(View.GONE);
        }else {
            container_controlls_Pview.setVisibility(View.GONE);
            container_controlls_Pedit.setVisibility(View.VISIBLE);
        }

        presenter_profile = new Presenter_Profile(getContext(), this, dataPetSelected);

        initRecyclerView();
        return v;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void InitBadgeMesagges(int mesagges_num) {
        badgeMessages = BadgeDrawable.create(this.getContext());
        BadgeUtils.attachBadgeDrawable(badgeMessages, getActivity().findViewById(R.id.toolbar), R.id.btn_mesaages);

        badgeMessages.setVisible(true);
        badgeMessages.setNumber(mesagges_num);
    }
    public void HiddenBadgeMesagges(){
        badgeMessages.setVisible(false);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener pageLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            int padding = recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.width_space);

            width_screen = recyclerView.getWidth();

            recyclerView.setPadding(padding, padding / 2, padding, padding / 2);

            // Once data has been obtained, this listener is no longer needed, so remove it...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    };

    @Override
    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),number_colms);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(pageLayoutListener);
    }

    @Override
    public Adapter_Profile_Posted_Pics initAdapter(ArrayList<DataPostedPic> list_profile_pics) {
        adapter_profilePostedPics = new Adapter_Profile_Posted_Pics(list_profile_pics, getActivity(), width_screen, number_colms);
        return adapter_profilePostedPics;
    }

    @Override
    public void startAdapter(Adapter_Profile_Posted_Pics adapter_profilePostedPics) {
        recyclerView.setAdapter(adapter_profilePostedPics);

    }

    @Override
    public void informationPet(DataPet dataPet, String posts, int type_profile) {
        String imageProfileName = dataPet.getProfilePic();
        int imageResourcePosted = getActivity().getResources().getIdentifier(imageProfileName, "drawable", getContext().getPackageName());
        img_profile.setImageResource(imageResourcePosted);
        txt_name_breed_Pet.setText(dataPet.getName() + " / " + dataPet.getBreed());
        txt_followers.setText(dataPet.getFollowers());
        txt_following.setText(dataPet.getFollowing());
        txt_posts.setText(posts);

        if(dataPet.getDescription() == null || dataPet.getDescription().equals(""))
            if (type_profile == 0)
                txt_description_edit.setText(getString(R.string.Enter_Description));
            else
                txt_description_view.setText("");
        else
            if (type_profile == 0)
                txt_description_edit.setText(dataPet.getDescription());
            else
                txt_description_view.setText(dataPet.getDescription());
    }
}
