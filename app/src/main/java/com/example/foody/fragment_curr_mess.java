package com.example.foody;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foody.model.curmes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_curr_mess extends Fragment {
    private View memberView;
    private DatabaseReference mref,cref,lref;
    private FirebaseAuth mAuth;
    TextView name,address,mornto,mornfrom,evento,evenfrom,nameofowner,phone,fees,nocurmes;
    AppCompatButton attendance,foodmenu,renew;
    CircleImageView curmesspic;
    LinearLayout curmess;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberView=inflater.inflate(R.layout.fragment_curr_mess,container,false);
        initialize();
        mAuth=FirebaseAuth.getInstance();
        final String currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("memberusers").child(currentUser).child("currentMess");
        cref= FirebaseDatabase.getInstance().getReference().child("users");
        lref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {
                    //setViewLayout(R.layout.fragment_curr_mess);
                    nocurmes.setVisibility(View.INVISIBLE);
                    curmess.setVisibility(View.VISIBLE);
                    final String userId=dataSnapshot.child("mid").getValue().toString();
                    cref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                curmes mess=dataSnapshot.getValue(curmes.class);
                                try {
                                    name.setText(mess.getName().toString());
                                    nameofowner.setText(mess.getNameofowner().toString());
                                    address.setText(mess.getAddress().toString());
                                    phone.setText(mess.getPhone().toString());
                                    fees.setText(mess.getFees().toString());
                                    mornfrom.setText(mess.getMornfrom().toString());
                                    mornto.setText(mess.getMornto().toString());
                                    evenfrom.setText(mess.getEvenfrom().toString());
                                    evento.setText(mess.getEvento().toString());
                                    Uri uri = Uri.parse(mess.getProfilepic());
                                    Picasso.with(getActivity()).load(uri).into(curmesspic);
                                    //Uri uri = Uri.parse(mess.getCurimg().toString());
                                    //Glide.with(fragment_curr_mess.this).load(uri).dontAnimate().into(curmesspic);
                                    //Picasso.with(getActivity()).load(uri).into(curmesspic);
                                }
                                catch(Exception e){}
                                cref.child(userId).child("attendance").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String exp_date=dataSnapshot.child("expiry_date").getValue().toString();

                                        Date expiry_date=stringToDate(exp_date);

                                        Calendar c1=new GregorianCalendar();
                                        Date d1=c1.getTime();

                                        long compare=d1.compareTo(expiry_date);

                                        if(compare<0)
                                        {
                                            renew.setEnabled(false);
                                        }
                                        else
                                        {
                                            lref.child(currentUser).child("renewrequested").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.getChildrenCount()>0)
                                                    {
                                                        renew.setEnabled(false);
                                                    }
                                                    else
                                                    {
                                                        renew.setEnabled(true);
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
                                attendance.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getActivity(),userAttendance.class);
                                        intent.putExtra("id",userId);
                                        startActivity(intent);
                                    }
                                });
                                foodmenu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getActivity(),memberfood_menu.class);
                                        intent.putExtra("id",userId);
                                        startActivity(intent);
                                    }
                                });
                                renew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cref.child(userId).child("renewrequests").child(currentUser).setValue("1");
                                        lref.child(currentUser).child("renewrequested").child("mid").setValue(userId);
                                        Toast.makeText(getActivity(),"RENEW REQUEST SENT",Toast.LENGTH_SHORT).show();
                                        renew.setEnabled(false);
                                    }
                                });
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
        return memberView;
    }

    void initialize() {
        name = (TextView) memberView.findViewById(R.id.umessname);
        nameofowner = (TextView) memberView.findViewById(R.id.uownername);
        address = (TextView) memberView.findViewById(R.id.umessAddress);
        mornfrom = (TextView) memberView.findViewById(R.id.utmornfrom);
        mornto = (TextView) memberView.findViewById(R.id.utmornto);
        evenfrom = (TextView) memberView.findViewById(R.id.utevenfrom);
        evento = (TextView) memberView.findViewById(R.id.utevento);
        phone = (TextView) memberView.findViewById(R.id.utphone_no);
        fees = (TextView) memberView.findViewById(R.id.umessfees);
        attendance = (AppCompatButton) memberView.findViewById(R.id.uattendance);
        foodmenu = (AppCompatButton) memberView.findViewById(R.id.utodaysmenu);
        renew = (AppCompatButton) memberView.findViewById(R.id.renew);
        curmesspic = (CircleImageView) memberView.findViewById(R.id.p_img);
        curmess=(LinearLayout)memberView.findViewById(R.id.curmes);
        nocurmes=(TextView)memberView.findViewById(R.id.nocurrmess);
    }

    private Date stringToDate(String aDate) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
//    private void setViewLayout(int id){
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        memberView = inflater.inflate(id,null);
//        ViewGroup rootView = (ViewGroup) getView();
//        rootView.removeAllViews();
//        rootView.addView(memberView);
//    }
}
