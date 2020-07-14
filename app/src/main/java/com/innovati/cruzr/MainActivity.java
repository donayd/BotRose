package com.innovati.cruzr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ubtechinc.cruzr.sys.cruzrleisure.leisure.LeisureManager;
import com.ubtrobot.Robot;
import com.ubtrobot.async.DoneCallback;
import com.ubtrobot.async.FailCallback;
import com.ubtrobot.emotion.EmotionException;
import com.ubtrobot.emotion.EmotionManager;
import com.ubtrobot.resource.Resource;
import com.ubtrobot.resource.ResourceException;
import com.ubtrobot.resource.ResourceManager;
import com.ubtrobot.resource.ResourceTypes;
import com.ubtrobot.resource.emotion.EmotionProperties;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView textBox;
    private List<Uri> mEmotionUriList;
    private EmotionManager mEmotionManager;
    private ResourceManager mResourceManager;
    private static final String TAG = "Manager-Emotion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textBox = findViewById(R.id.textView);

        checkPermissions();
        getBundle();

        Robot.initialize(MainActivity.this);
        LeisureManager.get().init(MainActivity.this);

        mEmotionUriList = new ArrayList<>();
        mEmotionManager = Robot.globalContext().getSystemService(EmotionManager.SERVICE);
        mResourceManager = Robot.globalContext().getSystemService(ResourceManager.SERVICE);

        // getEmotionList();
    }

    public void getEmotionList() {
        mResourceManager.getResourceList(ResourceTypes.EMOTION)
                .done(new DoneCallback<List<Resource<Parcelable>>>() {
                    @Override
                    public void onDone(List<Resource<Parcelable>> resources) {
                        mEmotionUriList.clear();

                        for (Resource<Parcelable> resource : resources) {
                            mEmotionUriList.add(resource.getUri());
                        }

                        Log.i(TAG, "Call getEmotionList: done:" + mEmotionUriList.size());
                        // expressEmotion();

                    }
                })
                .fail(new FailCallback<ResourceException>() {
                    @Override
                    public void onFail(ResourceException e) {
                        Log.i(TAG, "Call getEmotionList: fail:" + e.getMessage());
                    }
                });
    }

    public void expressEmotion() {
        if(mEmotionUriList.size() > 1) {
            mEmotionManager.express(mEmotionUriList.get(0))
                    .done(new DoneCallback<Void>() {
                        @Override
                        public void onDone(Void aVoid) {
                            Log.i(TAG, "Call expressEmotion: done.");
                        }
                    })
                    .fail(new FailCallback<EmotionException>() {
                        @Override
                        public void onFail(EmotionException e) {
                            Log.i(TAG, "Call expressEmotion: fail:" + e.getMessage());
                        }
                    });
        }
    }

    public void dismissEmotion(View view) {
        mEmotionManager.dismiss()
                .done(new DoneCallback<Void>() {
                    @Override
                    public void onDone(Void aVoid) {
                        Log.i(TAG, "Call dismissEmotion: done.");
                    }
                })
                .fail(new FailCallback<EmotionException>() {
                    @Override
                    public void onFail(EmotionException e) {
                        Log.i(TAG, "Call dismissEmotion: fail:" + e.getMessage());
                    }
                });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_PHONE_STATE}, 1000);
        }
    }

    private void getBundle() {

        String cedula = getIntent().getStringExtra("cedula");
        if (cedula == null) {
            textBox.setText("Bienvenido a CRUZR");
        } else {
            textBox.setText(cedula);
        }

    }

    public void activarScanner(View view) {
        Intent intent = new Intent(MainActivity.this, ReaderPDF417.class);
        startActivity(intent);
    }
}