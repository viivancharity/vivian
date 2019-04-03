package com.example.student.bcs3_group1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView tv;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        getSupportActionBar().setTitle("menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv=(TextView ) findViewById(R.id.textView21);


        tv.setText( getIntent().getStringExtra("NAME"));


    }}
