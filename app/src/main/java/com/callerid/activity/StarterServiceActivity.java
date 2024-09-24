package com.callerid.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.callerid.service.StarterService;

public class StarterServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        StarterService.onStartService(this);
        finish();
    }
}