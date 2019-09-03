package com.example.foody;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Extend_Att extends AppCompatActivity {
    private String userID,meals,Currentuser;
    EditText editTextNo;
    Button extend;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    private String expiry_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend__att);
        userID=getIntent().getExtras().get("id").toString();
        mAuth=FirebaseAuth.getInstance();
        Currentuser=mAuth.getCurrentUser().getUid().toString();
            mref = FirebaseDatabase.getInstance().getReference().child("users").child(Currentuser).child("attendance").child(userID).child("expiry_date");
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    expiry_date = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        editTextNo=(EditText)findViewById(R.id.Meals);
        extend=(Button)findViewById(R.id.extend);
        extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String extended_days=editTextNo.getText().toString();

                Date temp=stringToDate(expiry_date);
                Calendar c1=new GregorianCalendar();
                c1.setTime(temp);
                c1.add(Calendar.DATE, Integer.parseInt(extended_days));
                Date d1=c1.getTime();
                String date1=d1.toString();
                mref.setValue(date1);
                extend.setEnabled(false);
            }
        });
    }
    private Date stringToDate(String aDate) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
}
