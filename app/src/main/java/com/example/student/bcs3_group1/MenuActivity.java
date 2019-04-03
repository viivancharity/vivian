package com.example.student.bcs3_group1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;



public class MenuActivity extends AppCompatActivity {

    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    public Button button;
    public Button number;
    public EditText message;


    /*/code for passing message*/

/*
    String description = "Android Developer";
    EditText edit;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.message);
        but = (Button) findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namevalue = edit.getText().toString();
                Intent intent = new Intent(MenuActivity.this, SecondActivity.class);
                intent.putExtra("NAME", namevalue);
                intent.putExtra("DESCRIPTION", description);
                startActivity(intent);
            }
        });

    }

   */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        getSupportActionBar().setTitle("menu");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button =(Button) findViewById(R.id.button5);
        final EditText message = (EditText) findViewById(R.id.text1);
        button.setEnabled(true);

        if(checkPermission(Manifest.permission.SEND_SMS)){
            button.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[ ] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE );
        }

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onClick(View v) {

                /*passing data*/

                String namevalue = message.getText().toString();
                Intent intent = new Intent(MenuActivity.this, SecondActivity.class);
                intent.putExtra("NAME", namevalue);
                startActivity(intent);

                String msg = message.getText().toString();
                String number = "0701608537";

                message.getText().clear();
                Toast.makeText(MenuActivity.this, "Message sent", Toast.LENGTH_SHORT).show();



                if (!TextUtils.isEmpty(msg)) {
                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, msg, null, null);
                    } else {
                        Toast.makeText(MenuActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MenuActivity.this, "Enter a message", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


    private boolean checkPermission(String permission){
        int checkPermission= ContextCompat.checkSelfPermission(this, permission);
        return  checkPermission== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SEND_SMS_PERMISSION_REQUEST_CODE :
                if (grantResults.length>0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                  button.setEnabled(true);

                }

                break;
        }
    }
}
