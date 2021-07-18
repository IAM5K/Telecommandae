package com.example.owner.combined;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;


public class UpdateActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
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
        mTextUsername = (EditText)findViewById(R.id.oldPIN);
        mTextPassword = (EditText)findViewById(R.id.newPIN);
        mTextCnfPassword = (EditText)findViewById(R.id.cnfPIN);
        mButtonUpdate= (Button)findViewById(R.id.button_update);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                if(pwd.isEmpty()||cnf_pwd.isEmpty()){
                    Toast.makeText(UpdateActivity.this,"Error : Empty Field(s) ",Toast.LENGTH_SHORT).show();
                }
                else {

                    Boolean res = db.checkUser(user);
                    if(res == true) {
                        if (pwd.length()==5) {
                            if (pwd.equals(cnf_pwd)) {
                                boolean val = db.updateUser(pwd);
                                if (val) {
                                    Toast.makeText(UpdateActivity.this, "PIN successfully updated", Toast.LENGTH_SHORT).show();
                                    Intent moveToLogin = new Intent(UpdateActivity.this, LoginActivity.class);
                                    startActivity(moveToLogin);
                                }
                            }
                            else {
                                    Toast.makeText(UpdateActivity.this, "Pin Update Error", Toast.LENGTH_SHORT).show();
                                 }
                    }
                        else
                        {
                            Toast.makeText(UpdateActivity.this,"Error : PIN Length must be 5 characters only",Toast.LENGTH_SHORT).show();

                        }

                }
                    else{
                        Toast.makeText(UpdateActivity.this,"Old PIN is Wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    @Override
    protected void onResume() {
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
        super.onResume();

    }
}