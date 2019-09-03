package com.example.foody;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.example.foody.model.edit_member_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class member_details extends AppCompatActivity {
    private String userID,name,phone,address,workplace;
    TextView mname,mphone,maddress,mworkplace;
    CircleImageView img;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    AppCompatButton see_attendance,extend_attendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        userID=getIntent().getExtras().get("id").toString();
        initialize();
        see_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(member_details.this,mem_attendance.class);
                intent.putExtra("id",userID);
                startActivity(intent);
            }
        });
        extend_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(member_details.this,Extend_Att.class);
                intent.putExtra("id",userID);
                startActivity(intent);
            }
        });
        mref= FirebaseDatabase.getInstance().getReference().child("memberusers").child(userID);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edit_member_info info=dataSnapshot.getValue(edit_member_info.class);
                mname.setText(info.getName().toString());
                maddress.setText(info.getAddress().toString());
                mphone.setText(info.getPhone().toString());
                mworkplace.setText(info.getWorkplace().toString());
                try {
                    Uri uri = Uri.parse(info.getProfilepic().toString());
                    Picasso.with(getApplicationContext()).load(uri).into(img);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void initialize()
    {
        img=(CircleImageView)findViewById(R.id.mimg);
        mname=(TextView)findViewById(R.id.kname_of_member);
        mphone=(TextView)findViewById(R.id.kphone);
        mworkplace=(TextView)findViewById(R.id.memworkplace);
        maddress=(TextView)findViewById(R.id.Address_of_mem);
        see_attendance=(AppCompatButton)findViewById(R.id.seeattendance);
        extend_attendance=(AppCompatButton)findViewById(R.id.extendattendance);
    }
}
