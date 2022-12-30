package edu.uda.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.view.profile.View_Profile_Fragment;

public class ProfileView extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    Fragment fragmentProfileView;
    Toolbar toolbar;

    private static String controls_Pview = "activate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        fragmentProfileView = new View_Profile_Fragment();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null ){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Bundle dataPetSelected = getIntent().getExtras();

        Bundle dataFragment = new Bundle();
        dataFragment.putString(this.getResources().getString(R.string.Key_Data_FProfile), this.getResources().getString(R.string.Key_View_FProfile));
        dataFragment.putBundle("bundlePetSelected", dataPetSelected);
        fragmentProfileView.setArguments(dataFragment);

        getSupportFragmentManager().beginTransaction().add(R.id.containerProfileView, fragmentProfileView).commit();
    }

}