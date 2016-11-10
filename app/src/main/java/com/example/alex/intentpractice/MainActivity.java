package com.example.alex.intentpractice;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageViewGetPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewGetPhoto = (ImageView) findViewById(R.id.image_from_camera);


        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(headSetReceiver, new IntentFilter(AudioManager.ACTION_HEADSET_PLUG));
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_SHORT).show();
                        break;

                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver headSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(intent.ACTION_HEADSET_PLUG)) {

                switch (intent.getIntExtra("state", -1)) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "HeadSet pluged in", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(getApplicationContext(), "HeadSet unpluged", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }
    };


    @Override
    public void onClick(View view) {

    }

    public void getCameraImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            imageViewGetPhoto.setImageBitmap(img);

        } else if (resultCode == 2) {
            Toast.makeText(this, "Starting camera", Toast.LENGTH_SHORT).show();
            getCameraImage();
        } else if (resultCode == 3) {
            Log.d("log", "Gallery");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);

        } else if (requestCode==4){
            Log.d("log","Gallery pick");
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageViewGetPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplicationContext()).inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_camera:
                Log.d("log", "menu camera clicked");

                Intent i = new Intent(this, CameraGalery.class);
                startActivityForResult(i, 2);
                break;
            case R.id.menu_bt:
                BluetoothAdapter BA;
                BA = BluetoothAdapter.getDefaultAdapter();
                if (BA.isEnabled()) {
                    BA.disable();

                } else {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
        unregisterReceiver(headSetReceiver);
    }
}
