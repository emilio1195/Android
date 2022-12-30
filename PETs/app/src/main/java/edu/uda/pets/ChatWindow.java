package edu.uda.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

public class ChatWindow extends AppCompatActivity {
    TextView txtName, txtBreed;
    CircularImageView img_profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        img_profile_pic = (CircularImageView) findViewById(R.id.img_profile_pic);
        txtName = (TextView) findViewById(R.id.txtName_chat);
        txtBreed = (TextView) findViewById(R.id.txtBreed_pet_chat);

        Bundle dataRecived = this.getIntent().getExtras();
        String name = dataRecived.getString("name");
        String breed = dataRecived.getString("breed");
        String profile_pic = dataRecived.getString("profile_pic");
        int id = getResources().getIdentifier(profile_pic, "drawable", getPackageName());
        img_profile_pic.setImageResource(id);
        txtName.setText(name);
        txtBreed.setText(breed);

    }
}