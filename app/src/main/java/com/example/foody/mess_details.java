package com.example.foody;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foody.model.add1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class mess_details extends AppCompatActivity implements View.OnClickListener {
    private String userID,mname,maddress,mevenfrom,mevento,mfees,mmornfrom,mmornto,mnameofowner,mphone,mmessimg,wmenu;
    TextView umname,umaddress,umevenfrom,umevento,umfees,ummornfrom,ummornto,umnameofowner,umphone,daily,weeklymenu;
    CircleImageView ummessimg;
    private DatabaseReference mref,lref,cref,chref,kref,menuref,wref;
    FirebaseAuth mAuth;
    AppCompatButton btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_details);
        userID=getIntent().getExtras().get("id").toString();
        mname=getIntent().getExtras().get("mname").toString();
        mAuth=FirebaseAuth.getInstance();
        initialize();
        //Toast.makeText(mess_details.this,""+mname,Toast.LENGTH_SHORT).show();
        umname.setText(""+mname);
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                add1 info=dataSnapshot.getValue(add1.class);
                try {
                    //umname.setText(info.getName().toString());
                    umaddress.setText(info.getAddress().toString());
                    umphone.setText(info.getPhone().toString());
                    ummornfrom.setText(info.getMornfrom().toString());
                    ummornto.setText(info.getMornto().toString());
                    umevenfrom.setText(info.getEvenfrom().toString());
                    umevento.setText(info.getEvento().toString());
                    umnameofowner.setText(info.getNameofowner().toString());
                    umfees.setText(info.getFees().toString());
                    Uri uri = Uri.parse(info.getProfilepic());
                    Picasso.with(getApplicationContext()).load(uri).into(ummessimg);


                }
                catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void initialize()
    {
        umname=(TextView)findViewById(R.id.uname_of_member);
        umnameofowner=(TextView)findViewById(R.id.uname_of_owner);
        ummornfrom=(TextView)findViewById(R.id.umornfrom);
        ummornto=(TextView)findViewById(R.id.umornto);
        umaddress=(TextView)findViewById(R.id.uAddress_of_mess);
        umevenfrom=(TextView)findViewById(R.id.uevenfrom);
        umevento=(TextView)findViewById(R.id.uevento);
        umfees=(TextView)findViewById(R.id.ufees_of_mess);
        umphone=(TextView)findViewById(R.id.uphone_no);
        ummessimg=(CircleImageView)findViewById(R.id.choose_img);
        btn_signup=(AppCompatButton)findViewById(R.id.ubtn_signup);
        daily=(TextView)findViewById(R.id.menu);
        weeklymenu=(TextView)findViewById(R.id.weeklymenu);
        btn_signup.setOnClickListener(this);
        String cUser=mAuth.getCurrentUser().getUid();
        cref=FirebaseDatabase.getInstance().getReference().child("memberusers").child(cUser).child("requested");
        kref=FirebaseDatabase.getInstance().getReference().child("memberusers").child(cUser).child("currentMess");
        menuref=FirebaseDatabase.getInstance().getReference().child("menus").child(cUser).child("daily");
        wref=FirebaseDatabase.getInstance().getReference().child("users").child(cUser).child("weeklymenu");
        //Log.d("kp", "initialize: 1");
        cref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("kp", "initialize: 2");
                    if(dataSnapshot.getChildrenCount()>0)
                    {
                        if(dataSnapshot.child("mid").getValue().toString().equals(userID))
                        {
                            //Log.d("kp", "initialize: 4");
                            btn_signup.setText("Already requested");
                           btn_signup.setEnabled(false);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("menus").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String dailymenu=dataSnapshot.child("daily").getValue().toString();
                    daily.setText(dailymenu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        kref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {
                    if(dataSnapshot.child("mid").getValue().toString().equals(userID))
                    {
                        btn_signup.setText("Already a Member");
                        btn_signup.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("weeklymenu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {

                        String wmenu = dataSnapshot.child("monmorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("moneve").getValue().toString();
                        wmenu+="\n";

                        wmenu+=dataSnapshot.child("tuemorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("tueeve").getValue().toString();
                        wmenu+="\n";

                        wmenu+=dataSnapshot.child("wedmorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("wedeve").getValue().toString();
                        wmenu+="\n";

                        wmenu+=dataSnapshot.child("thurmorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("thureve").getValue().toString();
                        wmenu+="\n";

                        wmenu+=dataSnapshot.child("frimorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("frieve").getValue().toString();
                        wmenu+="\n";


                        wmenu+=dataSnapshot.child("satmorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("sateve").getValue().toString();
                        wmenu+="\n";

                        wmenu+=dataSnapshot.child("sunmorn").getValue().toString();
                        wmenu+="  ";
                        wmenu+= dataSnapshot.child("suneve").getValue().toString();
                        wmenu+="\n";




                    weeklymenu.setText(wmenu);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ubtn_signup:
                chref= FirebaseDatabase.getInstance().getReference().child("users");
                lref=FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("memrequests");
                final String currentUserID=mAuth.getCurrentUser().getUid();
                cref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0)
                        {
                            final String kid=dataSnapshot.child("mid").getValue().toString();
                            new AlertDialog.Builder(mess_details.this)
                                    .setTitle("Change Of Mess")
                                    .setMessage("You have already requested for some other mess. Requesting for this one will cancel your previous request. Continue?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HashMap<String,String> profileMap=new HashMap<>();
                                            profileMap.put("mid",userID);
                                            cref.setValue(profileMap);
                                            chref.child(kid).child("memrequests").child(currentUserID).setValue(null);
                                            lref.child(currentUserID).setValue("1");
                                            btn_signup.setText("Requested");
                                            btn_signup.setEnabled(false);
                                            Toast.makeText(getApplicationContext(),"Request has been sent",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                        else {
                            kref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount()>0)
                                    {
                                        final String kid=dataSnapshot.child("mid").getValue().toString();
                                        new AlertDialog.Builder(mess_details.this)
                                                .setTitle("Change Of Mess")
                                                .setMessage("You are already member of some other mess. If the Request gets accepted for this one, then your membership from previous one will get cancelled (No Refund of remaining money). Continue?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        HashMap<String,String> profileMap=new HashMap<>();
                                                        profileMap.put("mid",userID);
                                                        cref.setValue(profileMap);
                                                        chref.child(kid).child("memrequests").child(currentUserID).setValue(null);
                                                        lref.child(currentUserID).setValue("1");
                                                        btn_signup.setText("Requested");
                                                        btn_signup.setEnabled(false);
                                                        Toast.makeText(getApplicationContext(),"Request has been sent",Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create()
                                                .show();
                                    }
                                    else
                                    {
                                        HashMap<String,String> profileMap=new HashMap<>();
                                        profileMap.put("mid",userID);
                                        cref.setValue(profileMap);
                                        lref.child(currentUserID).setValue("1");
                                        btn_signup.setText("Requested");
                                        btn_signup.setEnabled(false);
                                        Toast.makeText(getApplicationContext(),"Request has been sent",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
//                cref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.getChildrenCount()>0)
//                        {
//                            final String kid=dataSnapshot.child("mid").getValue().toString();
//                            HashMap<String,String> profileMap=new HashMap<>();
//                            profileMap.put("mid",userID);
//                            cref.setValue(profileMap);
//                            chref.child(kid).child("memrequests").child(currentUserID).setValue(null);
//                            lref.child(currentUserID).setValue("1");
//                            btn_signup.setText("Requested");
//                            btn_signup.setEnabled(false);
//                            Toast.makeText(getApplicationContext(),"Request has been sent",Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            HashMap<String,String> profileMap=new HashMap<>();
//                            profileMap.put("mid",userID);
//                            cref.setValue(profileMap);
//                            lref.child(currentUserID).setValue("1");
//                            btn_signup.setText("Requested");
//                            btn_signup.setEnabled(false);
//                            Toast.makeText(getApplicationContext(),"Request has been sent",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

        }
    }
}
