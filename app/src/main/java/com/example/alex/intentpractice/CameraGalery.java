package com.example.alex.intentpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CameraGalery extends AppCompatActivity implements View.OnClickListener {
    TextView camera,gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_galery);

        camera = (TextView)findViewById(R.id.from_camera);
        camera.setOnClickListener(this);

        gallery = (TextView)findViewById(R.id.from_gallery);
        gallery.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.from_camera:

                setResult(2);
                finish();
                break;
            case R.id.from_gallery:

                setResult(3);
                finish();
            break;
        }
    }
}
