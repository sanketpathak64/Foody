package com.example.foody;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foody.model.weekly_menu1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class weeklymenu extends AppCompatActivity {
    private EditText monmorn,tuemorn,wedmorn,thurmom,frimorn,satmorn,sunmorn,moneve,tueeve,wedeve,thureve,frieve,sateve,suneve;
    Button update;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklymenu);
        mAuth=FirebaseAuth.getInstance();
        String currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("weeklymenu");
        monmorn=(EditText)findViewById(R.id.monday_morning);
        tuemorn=(EditText)findViewById(R.id.tuesday_morning);
        wedmorn=(EditText)findViewById(R.id.wednesday_morning);
        thurmom=(EditText)findViewById(R.id.thursday_morning);
        frimorn=(EditText)findViewById(R.id.friday_morning);
        satmorn=(EditText)findViewById(R.id.saturday_morning);
        sunmorn=(EditText)findViewById(R.id.sunday_morning);
        moneve=(EditText)findViewById(R.id.monday_evening);
        tueeve=(EditText)findViewById(R.id.tuesday_evening);
        wedeve=(EditText)findViewById(R.id.wednesday_evening);
        thureve=(EditText)findViewById(R.id.thursday_evening);
        frieve=(EditText)findViewById(R.id.friday_evening);
        sateve=(EditText)findViewById(R.id.saturday_evening);
        suneve=(EditText)findViewById(R.id.sunday_evening);
        update=(Button)findViewById(R.id.update_weekly);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mmonmorn,mtuemorn,mwedmorn,mthurmom,mfrimorn,msatmorn,msunmorn,mmoneve,mtueeve,mwedeve,mthureve,mfrieve,msateve,msuneve;
                mmonmorn=monmorn.getText().toString();
                mmoneve=moneve.getText().toString();
                mtuemorn=tuemorn.getText().toString();
                mtueeve=tueeve.getText().toString();
                mwedmorn=wedmorn.getText().toString();
                mwedeve=wedeve.getText().toString();
                mthurmom=thurmom.getText().toString();
                mthureve=thureve.getText().toString();
                mfrimorn=frimorn.getText().toString();
                mfrieve=frieve.getText().toString();
                msatmorn=satmorn.getText().toString();
                msateve=sateve.getText().toString();
                msunmorn=sunmorn.getText().toString();
                msuneve=suneve.getText().toString();
                if(mmonmorn.isEmpty())
                {
                    monmorn.setError("Menu is Required");
                    monmorn.requestFocus();
                    return;
                }
                if(mmoneve.isEmpty())
                {
                    moneve.setError("Menu is Required");
                    moneve.requestFocus();
                    return;
                }
                if(mtuemorn.isEmpty())
                {
                    tuemorn.setError("Menu is Required");
                    tuemorn.requestFocus();
                    return;
                }
                if(mtueeve.isEmpty())
                {
                    tueeve.setError("Menu is Required");
                    tueeve.requestFocus();
                    return;
                }
                if(mwedmorn.isEmpty())
                {
                    wedmorn.setError("Menu is Required");
                    wedmorn.requestFocus();
                    return;
                }
                if(mwedeve.isEmpty())
                {
                    wedeve.setError("Menu is Required");
                    wedeve.requestFocus();
                    return;
                }
                if(mthurmom.isEmpty())
                {
                    thurmom.setError("Menu is Required");
                    thurmom.requestFocus();
                    return;
                }
                if(mthureve.isEmpty())
                {
                    thureve.setError("Menu is Required");
                    thureve.requestFocus();
                    return;
                }
                if(mfrimorn.isEmpty())
                {
                    frimorn.setError("Menu is Required");
                    frimorn.requestFocus();
                    return;
                }
                if(mfrieve.isEmpty())
                {
                    frieve.setError("Menu is Required");
                    frieve.requestFocus();
                    return;
                }
                if(msatmorn.isEmpty())
                {
                    satmorn.setError("Menu is Required");
                    satmorn.requestFocus();
                    return;
                }
                if(msateve.isEmpty())
                {
                    sateve.setError("Menu is Required");
                    sateve.requestFocus();
                    return;
                }
                if(msunmorn.isEmpty())
                {
                    sunmorn.setError("Menu is Required");
                    sunmorn.requestFocus();
                    return;
                }
                if(msuneve.isEmpty())
                {
                    suneve.setError("Menu is Required");
                    suneve.requestFocus();
                    return;
                }
                HashMap<String,Object> profileMap=new HashMap<>();
                profileMap.put("monmorn",mmonmorn);
                profileMap.put("moneve",mmoneve);
                profileMap.put("tuemorn",mtuemorn);
                profileMap.put("tueeve",mtueeve);
                profileMap.put("wedmorn",mwedmorn);
                profileMap.put("wedeve",mwedeve);
                profileMap.put("thurmorn",mthurmom);
                profileMap.put("thureve",mthureve);
                profileMap.put("frimorn",mfrimorn);
                profileMap.put("frieve",mfrieve);
                profileMap.put("satmorn",msatmorn);
                profileMap.put("sateve",msateve);
                profileMap.put("sunmorn",msunmorn);
                profileMap.put("suneve",msuneve);
                mref.updateChildren(profileMap);
                Toast.makeText(getApplicationContext(),"Update Successfull",Toast.LENGTH_SHORT).show();
            }
        });
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    weekly_menu1 menu = dataSnapshot.getValue(weekly_menu1.class);
                    monmorn.setText(menu.getMonmorn());
                    moneve.setText(menu.getMoneve());
                    tuemorn.setText(menu.getTuemorn());
                    tueeve.setText(menu.getTueeve());
                    wedmorn.setText(menu.getWedmorn());
                    wedeve.setText(menu.getWedeve());
                    thurmom.setText(menu.getThurmorn());
                    thureve.setText(menu.getThureve());
                    frimorn.setText(menu.getFrimorn());
                    frieve.setText(menu.getFrieve());
                    satmorn.setText(menu.getSatmorn());
                    sateve.setText(menu.getSateve());
                    sunmorn.setText(menu.getSunmorn());
                    suneve.setText(menu.getSuneve());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
