package edu.bupt.ticketextraction;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.bupt.ticketextraction.camera.Shoot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button cameraBtn = findViewById(R.id.camera_btn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shoot s = new Shoot();
                s.dispatchTakePictureIntent();
            }
        });
    }


}