package com.example.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /** Default JPEG save quality. */
    private static final int DEFAULT_JPEG_QUALITY = 50;

    /** storage permission status. */
    private boolean canWriteToPublicStorage = false;
    /** Constant to request permission to store. */
    private static final int REQUEST_WRITE_STORAGE = 112;

    /** Current composite image. */
    private Bitmap bitmap = null;


    private static final String TAG = "MainActivity";

    Spinner degree;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate started");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button findImage = (Button) findViewById(R.id.button);

        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                should find the image and store it as something.
                 */
            }
        });

        degree = findViewById(R.id.spinner);
        adapter
                = ArrayAdapter.createFromResource(this, R.array.degrees, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree.setAdapter(adapter);
        degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                make the thingy bad
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*
        should set the amount.
         */

        Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentBitmap();
            }
        });

        canWriteToPublicStorage = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        Log.d(TAG, "Do we have permission to write to external storage: "
                + canWriteToPublicStorage);
        if (!canWriteToPublicStorage) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get a new file location for saving.
     *
     * @return the path to the new file or null of the create failed
     */
    File getSaveFilename() {
        String imageFileName = "Blurred" + new SimpleDateFormat("DDMMYYYY_HHmmss", Locale.US)
                .format(new Date());
        File storageDir;
        if (canWriteToPublicStorage) {
            storageDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            return null;
        }
    }


    private void saveCurrentBitmap() {
        if (bitmap == null) {
            return;
        }

        File saveFilename = getSaveFilename();
        try {
            assert saveFilename != null;
            FileOutputStream fileOutputStream = new FileOutputStream(saveFilename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_JPEG_QUALITY, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Problem saving photo",
                    Toast.LENGTH_LONG).show();
            Log.w(TAG, "Problem saving photo");
            return;
        }

        Toast.makeText(getApplicationContext(), "Photo saved as " + saveFilename,
                Toast.LENGTH_LONG).show();
        addPhotoToGallery(Uri.fromFile(saveFilename));
    }

    /**
     * Add a photo to the gallery so that we can use it later.
     *
     * @param toAdd URI of the file to add
     */
    void addPhotoToGallery(final Uri toAdd) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(toAdd);
        this.sendBroadcast(mediaScanIntent);
        Log.d(TAG, "Added photo to gallery: " + toAdd);
    }


}
