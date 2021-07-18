package com.example.owner.combined;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class VerificationActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText mVerify;
    Button buttonVerify;
    int x;
    String n;
    Button buttonBack;
    EditText mVCode;
    Button buttonProceed;
    String myNum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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
        mVerify = (EditText)findViewById(R.id.edittext_mymobile);
        buttonVerify = (Button) findViewById(R.id.button_verify);
        mVCode = (EditText)findViewById(R.id.edittext_VerifyCode);
        buttonProceed = (Button) findViewById(R.id.button_proceed);
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonProceed.setVisibility(View.GONE);
        mVCode.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myNum = mVerify.getText().toString().trim();
                if(myNum.isEmpty()){
                    Toast.makeText(VerificationActivity.this,"Error : Empty Field(s) ",Toast.LENGTH_SHORT).show();
                }

                else {
                       if (myNum.length()==10 ) {

                                    try {
                                        n = myNum;
                                        x=(int)( Math.random()*100000);
                                        String message="Your Verification Code is "+String.format("%5d",x)+"\n Regards,\nTélécommande";


                                        SmsManager.getDefault().sendTextMessage(n, null,
                                                message, null, null);
                                        buttonVerify.setVisibility(View.GONE);
                                        buttonProceed.setVisibility(View.VISIBLE);
                                        mVerify.setVisibility(View.GONE);
                                        mVCode.setVisibility(View.VISIBLE);
                                        buttonBack.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        AlertDialog.Builder alertDialogBuilder = new
                                                AlertDialog.Builder(VerificationActivity.this);
                                        AlertDialog dialog = alertDialogBuilder.create();
                                        dialog.setMessage(e.getMessage());
                                        dialog.show();
                                    }


                            }

                            else if (myNum.length()!=10)
                            {
                                Toast.makeText(VerificationActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                            }

                        }
                }

        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonVerify.setVisibility(View.VISIBLE);
                buttonProceed.setVisibility(View.GONE);
                buttonBack.setVisibility(View.GONE);
            }
        });
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = "" + x;
                String v=mVCode.getText().toString();
                if (v.equals(code)) {
//
                    Long mmm= db.Verify(n);
                    if(mmm>0) {
                        Toast.makeText(VerificationActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
                    }
                    Intent moveToRegister = new Intent(VerificationActivity.this, RegisterActivity.class);
                    startActivity(moveToRegister);
                }
                else
                {
                    Toast.makeText(VerificationActivity.this, "Wrong Verification Code", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
