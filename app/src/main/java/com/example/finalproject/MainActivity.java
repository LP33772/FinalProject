package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/*
I think we need to turn "send" to "save image" instead since you can hijack the MP1 info to do so.
I set the spinner so that when you have certain ones selected it'll start the image transformation but
at the signified amount.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * the factor determined by the spinner
     */
    public static int FACTOR = 1;

    /** Constant to request an image capture. */
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 1;


    /** Request queue for our network requests. */
    private static RequestQueue requestQueue = null;

    /** Constant to perform a read file request. */
    private static final int READ_REQUEST_CODE = 42;


    /**
     * Default JPEG save quality.
     */
    private static final int DEFAULT_JPEG_QUALITY = 50;

    /**
     * storage permission status.
     */
    private boolean canWriteToPublicStorage = false;
    /**
     * Constant to request permission to store.
     */
    private static final int REQUEST_WRITE_STORAGE = 112;

    /**
     * Current composite image.
     */
    public Bitmap bitmap = null;


    private static final String TAG = "MainActivity";

    Spinner degree;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView imageView = findViewById(R.id.imageV);
        imageView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bitmap != null) {
                setBitmap(bitmap);
            }
        });


        Log.d(TAG, "onCreate started");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /*
        https://stackoverflow.com/questions/5089300/how-can-i-change-the-image-of-an-imageview
         */

        /*
        https://www.youtube.com/watch?v=a7gxZKW4VkE
         */

        Button findImage = (Button) findViewById(R.id.button);

        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOpenFile();
                /*
                should find the image and store it as something.
                 */
            }
        });
        /*
        https://www.youtube.com/watch?v=28jA5-mO8K8
         */
        degree = findViewById(R.id.spinner);
        adapter
                = ArrayAdapter.createFromResource(this, R.array.degrees, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree.setAdapter(adapter);
        degree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    FACTOR = 64;
                } else if (position == 1) {
                    FACTOR = 32;
                } else if (position == 2) {
                    FACTOR = 16;
                } else if (position == 3) {
                    FACTOR = 8;
                } else if (position == 4) {
                    FACTOR = 4;
                }
                startProcessImage("blur");
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

        Button send = findViewById(R.id.send);

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

    private void startOpenFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    private void startProcessImage(final String action) {
        if (bitmap == null) {
            Toast.makeText(getApplicationContext(), "No image selected",
                    Toast.LENGTH_LONG).show();
            Log.w(TAG, "No image selected");
            return;
        }

        /*
         * Launch our background task which actually makes the request. It will call
         * setForegroundBitmap when it completes.
         */
        Bitmap toTransform = bitmap;
        new Tasks.ProcessImageTask(MainActivity.this, action).execute(toTransform);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Update the currently displayed image.
     * SEE HERE
     * I can't get this to work either sigh
     *
     * @param setBitmap the new bitmap to display
     */
    void setBitmap(final Bitmap setBitmap) {
        bitmap = setBitmap;
        final ImageView imageV = findViewById(R.id.imageV);
        imageV.setImageBitmap(bitmap);

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

    /**
     * URL storing the file to download.
     */
    private String downloadFileURL;


    private void startDownloadFile() {

        // Build a dialog that we will use to ask for the URL to the photo

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Download File");
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, unused) -> {

            // If the user clicks OK, try and download the file
            downloadFileURL = input.getText().toString().trim();
            Log.d(TAG, "Got download URL " + downloadFileURL);
            new Tasks.DownloadFileTask(MainActivity.this, requestQueue)
                    .execute(downloadFileURL);
        });
        builder.setNegativeButton("Cancel", (dialog, unused) -> dialog.cancel());


    }

    /*
    also taken from MP1 -- opening a file is REALLY complicated yeesh
     */

    /** Current file that we are using for our image request. */
    private boolean photoRequestActive = false;

    /** Whether a current photo request is being processed. */
    private File currentPhotoFile = null;

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {
        if (resultCode != Activity.RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                photoRequestActive = false;
            }
            return;
        }
        Uri currentPhotoURI;
        if (requestCode == READ_REQUEST_CODE) {
            currentPhotoURI = resultData.getData();
        } else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            currentPhotoURI = Uri.fromFile(currentPhotoFile);
            photoRequestActive = false;
        } else {
            return;
        }
        Log.d(TAG, "Photo selection produced URI " + currentPhotoURI);
        loadPhoto(currentPhotoURI);
    }

    /**
     * Load a photo and prepare for viewing.
     *
     * @param currentPhotoURI URI of the image to process
     */
    private void loadPhoto(final Uri currentPhotoURI) {

        if (currentPhotoURI == null) {
            Toast.makeText(getApplicationContext(), "No image selected",
                    Toast.LENGTH_LONG).show();
            Log.w(TAG, "No image selected");
            return;
        }
        String uriScheme = currentPhotoURI.getScheme();

        byte[] imageData;
        try {
            assert uriScheme != null;
            switch (uriScheme) {
                case "file":
                    imageData =
                            FileUtils.readFileToByteArray(new File(
                                    Objects.requireNonNull(currentPhotoURI.getPath())));
                    break;
                case "content":
                    InputStream inputStream = getContentResolver().openInputStream(currentPhotoURI);
                    assert inputStream != null;
                    imageData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Unknown scheme " + uriScheme,
                            Toast.LENGTH_LONG).show();
                    return;
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error processing file",
                    Toast.LENGTH_LONG).show();
            Log.w(TAG, "Error processing file: " + e);
            return;
        }


    }
