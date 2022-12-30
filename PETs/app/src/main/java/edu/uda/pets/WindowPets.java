package edu.uda.pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import edu.uda.pets.Adapter.ViewPageAdapter;
import edu.uda.pets.view.profile.IF_View_Profile;
import edu.uda.pets.view.profile.View_Profile_Fragment;
import edu.uda.pets.Fragmentos.Home_Fragment;
import edu.uda.pets.view.favorites.FavoritosPets;

//En esta clase se instancian los Valores o datos de cada mascota
public class WindowPets extends AppCompatActivity {

    //Toolbar myBar;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MenuItem itemMesagges;
    private View_Profile_Fragment view_profile_fragment;
    float contMesagges = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_pets);
       // myBar = (Toolbar) findViewById(R.id.userActionBar);
        //setActionBar(myBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vPager);
        if (toolbar != null ){
            setSupportActionBar(toolbar);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.btnfavoritos, menu);
        inflater.inflate(R.menu.btn_messages, menu);
        inflater.inflate(R.menu.menu_opcion, menu);
        SetUpViewPager();
        itemMesagges = menu.findItem(R.id.btn_mesaages);
        itemMesagges.setVisible(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    itemMesagges.setVisible(false);
                    if(view_profile_fragment!= null)
                        view_profile_fragment.HiddenBadgeMesagges();
                } else if (tab.getPosition() == 1) {
                    itemMesagges.setVisible(true);
                    view_profile_fragment.InitBadgeMesagges(Math.round(contMesagges));
                    contMesagges *= 1.5;
                }
                Log.d("UNSELECTED", "TAB SELECTED " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("UNSELECTED", "TAB UNSELECTED " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("RESELECTED", "TAB RESELECTED " + tab.getPosition());
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_favoritos:
                Intent favoritosVentana = new Intent(WindowPets.this, FavoritosPets.class);
                startActivity(favoritosVentana);
                return true;
            case R.id.btn_mesaages:
                Intent MessagesWindow = new Intent(WindowPets.this, Messages.class);
                startActivity(MessagesWindow);
                return true;
            case R.id.mContacto:
                Intent irContacto = new Intent(WindowPets.this, Contacto.class);
                startActivity(irContacto);
                return true;
            case R.id.mAbout:
                Intent irAbout = new Intent(WindowPets.this, AcercaDe.class);
                startActivity(irAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Fragment> AddFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();

        view_profile_fragment = new View_Profile_Fragment();
        Bundle dataFragment = new Bundle();
        dataFragment.putString(this.getResources().getString(R.string.Key_Data_FProfile), this.getResources().getString(R.string.Key_Edit_FProfile));
        view_profile_fragment.setArguments(dataFragment);

        fragments.add(new Home_Fragment());
        fragments.add(view_profile_fragment);
        return fragments;
    }

    private void SetUpViewPager(){
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), AddFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_action_inicio);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_perfil);
    }


}
