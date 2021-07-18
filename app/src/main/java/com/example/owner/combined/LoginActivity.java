package com.example.owner.combined;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    // For audio part
    private MediaPlayer objMediaPlayer;
    int i = 2, z;
    String address;
    String message;
    String myself;
    String friend;
    String friendPin;
    String namecsv = "";
    String phonecsv, checkKey = "";
    String checkTask = "";
    String myLoc = "";
    String namearray[];
    String phonearray[];
    String inaccessible="GPS is Off";
    ImageView info;
    int totalContacts = -1;
    TextView mTextViewRegister;
    TextView mTextViewUpdate;
    DatabaseHelper db;
    SQLiteDatabase mydb;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    @TargetApi(Build.VERSION_CODES.O)

    private CameraManager objCameraManager;
    private String mCameraId;
    private Button  disable, enable;
    TextView friendbtn;
    private static  final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    String latitude,longitude;
    DatabaseHelper controllerdb = new DatabaseHelper(this);
    /**
     * for getting torch mode
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }

        }
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }

        startService(new Intent(this,MyService.class));
        Bundle extras = getIntent().getExtras();
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
//
        /**
         * For Lock Part
         */
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        enable = (Button) findViewById(R.id.enablebtn);
        disable = (Button) findViewById(R.id.disablebtn);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableLock();
            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableLock();
            }
        });


        /**
         * Check if device contains flashlight
         *
         * if not then exit from screen
         *
         */


        objCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = objCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null);
        if (extras != null) {
            address = extras.getString("MessageNumber");
            message = extras.getString("Message");
            checkKey = (message.substring(0, 5));
            checkTask = (message.substring(5, 6));
            message = (message.substring(6, message.length()));
        }
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (name != null) {
                namecsv += name + ",";
                phonecsv += phoneNumber + ",";
                totalContacts++;
            }

        }
        phones.close();

        namearray = namecsv.split(",");
        phonearray = phonecsv.split(",");


        db = new DatabaseHelper(this);
        Cursor res = db.getAllData();
        mTextViewRegister = (TextView) findViewById(R.id.textview_register);
        mTextViewUpdate = (TextView) findViewById(R.id.textview_update);
        info=(ImageView)findViewById(R.id.info);
        friendbtn=(TextView) findViewById(R.id.textview_frnd);
        if (res.getCount() != 0) {

            mTextViewRegister.setVisibility(View.GONE);
            mTextViewUpdate.setVisibility(View.VISIBLE);
        }

        if (res.getCount() == 0) {

            mTextViewRegister.setVisibility(View.VISIBLE);
            mTextViewUpdate.setVisibility(View.GONE);
        }
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent veriifyIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(veriifyIntent);
            }
        });
        friendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friendIntent = new Intent(LoginActivity.this, TrustedCommunication.class);
                startActivity(friendIntent);
            }
        });
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent veriifyIntent = new Intent(LoginActivity.this, VerificationActivity.class);
                startActivity(veriifyIntent);
            }
        });

        mTextViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent(LoginActivity.this, UpdateActivity.class);
                startActivity(updateIntent);
            }
        });

        Boolean res1 = db.checkUser(checkKey);
        if (res1 == true) {

            if (checkTask.equals("C")) {
                display();
            }
            if (checkTask.equals("R")) {
                turnOnLight();
            }
            if (checkTask.equals("L")) {
                locate();
            }
            if (checkTask.equals("S")) {
                callMe();
                try {
                    Thread.sleep(3000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                LockScreen();
            }
            if (checkTask.equals("P")) {
                LockScreen();
            }
            if (checkTask.equals("H")) {
                help();
            }
            if (checkTask.equals("N")) {
                textOfriend();
            }
        } else {
            //Toast.makeText(LoginActivity.this, "Welcome to TeleCommande", Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.CALL_PHONE}, REQUEST_LOCATION);

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
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }
    String n;
    public void help()
    {
        message="Télécommande \nUser Guide:\n\n" +
                "R: Ring Mode\n" +
                "C: Contact Retrieval \n" +
                "L: Device Location\n"+
                "P: Protect Device\n" +
                "S: Call Me Back\n\n" +
                "Format: xxxxx<Command>";
        try {
            n = address;

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
    public void textOfriend()
    {
        String updatedPin="";
        try {
            updatedPin=newpin();
            message="Your Friend's New PIN is : "+updatedPin+"\n Regards,\n Télécommande";
            displayFriend();
            n=friend;

            SmsManager.getDefault().sendTextMessage(n, null,
                    message, null, null);
        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new
                    AlertDialog.Builder(this);
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.setMessage(e.getMessage());
            dialog.show();
        }
        try {
            message="Your New PIN is : "+updatedPin+"\n Regards,\n Télécommande";
            displayFriend();
            n=myself;

            SmsManager.getDefault().sendTextMessage(n, null,
                    message, null, null);
        } catch (Exception e) {
            AlertDialog.Builder alertDialogBuilder = new
                    AlertDialog.Builder(this);
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.setMessage(e.getMessage());
            dialog.show();
        }
        Toast.makeText(this,n , Toast.LENGTH_LONG).show();
    }

    public String newpin()
    {
        String np=""+String.format("%5d",getRandomNumber());

        db.updateUser(np);
        return np;
    }
    public int getRandomNumber(){
        int x =(int)( Math.random()*100000);
        int p=x;
        return p;
    }
    public void locate()
    {
       message=inaccessible;


        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check gps is enable or not

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

            OnGPS();
        }
        else
        {
            //GPS is already On then

            getLocation();
        }
        message=myLoc;
        try {
            n = address;

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


    public void display() {
        n = address;


        for (i = 0; i <= totalContacts; i++) {

            if (namearray[i].equals(message)) {
                String msg = phonearray[i];
                z = i;
                z = 10;
                message = msg + " is your needed contact";
                break;
            }
        }
        try {

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

    public void turnOnLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                objCameraManager.setTorchMode(mCameraId, true);
                playOnOffSound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playOnOffSound() {
        objMediaPlayer = MediaPlayer.create(LoginActivity.this, R.raw.alarm);
        objMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        objMediaPlayer.start();
    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                myLoc="Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude;
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                myLoc="Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude;
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                myLoc="Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude;
            }
            else
            {
                myLoc="Can't Get Your Location";
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void callMe()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String target=address;
        callIntent.setData(Uri.parse("tel:"+target));

        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(LoginActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void LockScreen()
    {
        boolean active = devicePolicyManager.isAdminActive(compName);

        if (active) {
            devicePolicyManager.lockNow();
        }
    }
   public void enableLock()
   {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
            startActivityForResult(intent, RESULT_ENABLE);

   }
   public void disableLock()
   {
            devicePolicyManager.removeActiveAdmin(compName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
   }

    private void displayFriend() {
        mydb = controllerdb.getReadableDatabase();
        Cursor cursor = mydb.rawQuery("SELECT * FROM  registeruser",null);
        cursor.moveToFirst();
        friend=""+(String)cursor.getString(cursor.getColumnIndex("number1"));
        myself= ""+(String)cursor.getString(cursor.getColumnIndex("verifyNum"));
        friendPin= ""+(String)cursor.getString(cursor.getColumnIndex("number1Pin"));
    }


}



