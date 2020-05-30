package com.example.nm_store_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class activity_edit extends AppCompatActivity {
        public String account = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle bundle = getIntent().getExtras();
        account = bundle.getString("account");
    }
}
