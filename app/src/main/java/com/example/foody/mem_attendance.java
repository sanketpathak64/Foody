package com.example.foody;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mem_attendance extends AppCompatActivity {
    private String userID,currentUser,meals;
    TextView mealsText,rd,ed;
    private DatabaseReference mref,eref,rref;
    private FirebaseAuth mAuth;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_attendance);
        userID=getIntent().getExtras().get("id").toString();
        mealsText=(TextView)findViewById(R.id.meals);
        rd=findViewById(R.id.rdate);
        ed=findViewById(R.id.edate);
        ll=findViewById(R.id.at);
        meals=mealsText.getText().toString();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance").child(userID).child("attend");
        eref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance").child(userID).child("expiry_date");
        rref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance").child(userID).child("renewed_date");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long cnt=dataSnapshot.getChildrenCount();
                int rem= (int) (58-cnt);
                String updatedmeals=meals+rem;
                mealsText.setText(updatedmeals);
                String date;
                for(int i=1;i<=cnt;i++)
                {
                    String l= (String) dataSnapshot.child(String.valueOf(i)).getValue();
                    int len=l.length();
                    if(l.charAt(len-1)=='M')
                    {
                        date=""+i+".  "+l+"orning";
                    }
                    else {
                        date=""+i+".  "+l+"vening";
                    }
                    TextView t1=new TextView(getApplicationContext());
                    t1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    t1.setTextSize(20);
                    t1.setTextColor(getResources().getColor(R.color.black));
                    t1.setText(date);
                    ll.addView(t1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        eref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ed.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    rd.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
