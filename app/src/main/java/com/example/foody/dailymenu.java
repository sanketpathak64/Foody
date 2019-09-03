package com.example.foody;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class dailymenu extends AppCompatActivity {
    EditText daily;
    Button update;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    String dailymenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymenu);
        daily=(EditText)findViewById(R.id.daily);
        update=(Button) findViewById(R.id.update_daily);
        mAuth=FirebaseAuth.getInstance();
        String currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("menus").child(currentUser);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailymenu=daily.getText().toString();
                if(dailymenu.isEmpty())
                {
                    daily.setError("Menu is Required");
                    daily.requestFocus();
                    return;
                }
                HashMap<String,Object> profileMap=new HashMap<>();
                profileMap.put("daily",dailymenu);
                mref.updateChildren(profileMap);
                Toast.makeText(getApplicationContext(),"Update Successfull",Toast.LENGTH_SHORT).show();
            }
        });
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("daily").exists()) {
                    String tmenu = dataSnapshot.child("daily").getValue().toString();
                    daily.setText(tmenu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
