package com.example.foody;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class memberfood_menu extends AppCompatActivity {
    TextView daily;
    String dailymenu,currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberfood_menu);
        daily=findViewById(R.id.menu);
        currentUser=getIntent().getExtras().get("id").toString();
        FirebaseDatabase.getInstance().getReference().child("menus").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    dailymenu=dataSnapshot.child("daily").getValue().toString();
                    daily.setText(dailymenu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
