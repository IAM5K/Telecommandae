package com.example.owner.combined;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class TrustedCommunication extends AppCompatActivity {
    SQLiteDatabase mydb;
    DatabaseHelper controllerdb = new DatabaseHelper(this);
    String friend;
    String friendPin;
    String n;
    String message;
    ImageView RM,LM,SM,PM;

    DatabaseHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trustedcommunication);
        final VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.finalclip);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

        db = new DatabaseHelper(this);
        RM = (ImageView) findViewById(R.id.imageViewR);
        SM = (ImageView) findViewById(R.id.imageViewS);
        PM = (ImageView) findViewById(R.id.imageViewP);
        LM = (ImageView) findViewById(R.id.imageViewL);
        RM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFriend();
                message=friendPin+"R";
                communicate();
            }
        });
        PM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFriend();
                message=friendPin+"P";
                communicate();
            }
        });
        SM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFriend();
                message=friendPin+"S";
                communicate();
            }
        });
        LM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFriend();
                message=friendPin+"L";
                communicate();
            }
        });
    }
    @Override
    protected void onResume() {
        final VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.finalclip);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
        super.onResume();
    }
    private void displayFriend() {
        mydb = controllerdb.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT * FROM  registeruser",null);
        cursor.moveToFirst();
        friend=""+(String)cursor.getString(cursor.getColumnIndex("number1"));
        friendPin= ""+(String)cursor.getString(cursor.getColumnIndex("number1Pin"));
    }


    private void communicate(){
        try {
            n = friend;

            SmsManager.getDefault().sendTextMessage(n, null,
                    message, null, null);
        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new
                    AlertDialog.Builder(this);
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.setMessage(e.getMessage());
            dialog.show();
        }
    }

}

