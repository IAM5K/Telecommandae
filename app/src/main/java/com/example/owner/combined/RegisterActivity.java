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

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;

    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText mFriendNumber;
    EditText mFriendNumberPIN;
    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        db = new DatabaseHelper(this);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        mFriendNumber = (EditText)findViewById(R.id.edittext_friendnumber);
        mFriendNumberPIN = (EditText)findViewById(R.id.edittext_friendnumberPin);
        mButtonRegister = (Button)findViewById(R.id.button_register);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                String num1 = mFriendNumber.getText().toString().trim();
               String num1Pin = mFriendNumberPIN.getText().toString().trim();

                if (num1Pin.isEmpty())
                {
                    num1Pin="null1";
                }
                if(pwd.isEmpty()||cnf_pwd.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Error : Empty Field(s) ",Toast.LENGTH_SHORT).show();
                }

                else {
                    if (pwd.length()==5) {
                        if (pwd.equals(cnf_pwd)) {
                            if (num1.length()==10) {
                                boolean check=db.addUser(pwd, num1,num1Pin);
                                if (check) {
                                    Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                                    Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(moveToLogin);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registeration Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (num1.length()!=10 && num1.length()!=0)
                                {
                                    Toast.makeText(RegisterActivity.this, "Invalid Trusted Number", Toast.LENGTH_SHORT).show();
                                }
                            else {
                                    Toast.makeText(RegisterActivity.this, "Trusted Number is Mandatory", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Error : PIN Length must be 5 characters only",Toast.LENGTH_SHORT).show();

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