package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.manage).setOnClickListener(this);
        findViewById(R.id.user).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.manage:
                startActivity(new Intent(this,login.class));
                break;
            case R.id.user:
                startActivity(new Intent(this,member_login.class));
                break;
        }
    }
}
